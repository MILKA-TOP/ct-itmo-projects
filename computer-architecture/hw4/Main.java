package elf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Map<String, String> symbol_types = new HashMap<>();
    private static final Map<String, String> symbol_binds = new HashMap<>();
    private static final Map<String, String> symbol_visibility = new HashMap<>();
    private static final Map<String, String> id_of_names = new HashMap<>();
    private static int position = 0;
    private static int text_sh_offset;      //Смещение .text
    private static int text_sh_size;        //Размер .text
    private static int sht_strtab_offset;//Смещение таблицы строк от начала
    private static int e_shoff;         //Cмещение таблицы заголовков секций
    private static int sh_offset;      //Смещение symTable
    private static int sh_size;         //Размер symTable
    private static int sh_entsize;      //Размер одной записи в symTable
    private static boolean hasOutFile = false;
    private static int[] tableOfElfFile;
    private static String[][] assembler_commands;
    private static String[][] symTable;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {             //т.к нет названия файла
            throw new FileNotFoundException();
        } else if (args.length == 2) {
            hasOutFile = true;
        }
        downloadingMaps();
        tableOfElfFile = readElfFile(args[0]);
        if (!thisIsElfFile()) {//Проверка на то, является ли данный файл ELF файлом
            System.out.println("This file is not .ELF file.");
            throw new FileNotFoundException();
        }
        position += 28; //Расстояние между смещение таблицы заголовков секций и сигнатуры файла
        e_shoff = gettingByteLine(4);
        position = e_shoff;
        int temp_type_of_section = 0;
        while (temp_type_of_section != 3) { //Нахождение секции с таблицой строк
            position += 4;
            temp_type_of_section = gettingByteLine(4);
            position += 8;
            sht_strtab_offset = gettingByteLine(4);
            position += 20;
        }
        int tempPosition = position;
        gettingInformationAboutSymTable();
        gettingSymTable();
        int sh_name = 0; //смещение ".text"
        position = tempPosition;
        position = gettingOffset();
        int partSectionSet = 0;
        StringBuilder tempCheckingInSectionName = new StringBuilder();
        while (!tempCheckingInSectionName.toString().contains(".text")) {
            tempCheckingInSectionName = new StringBuilder();
            do {
                sh_name++;
                tempCheckingInSectionName.append((char) tableOfElfFile[position]);
            } while (tableOfElfFile[position++] != 0);
        }

        position = e_shoff;

        while (partSectionSet + 6 != sh_name) {//поиск информации о секции .text
            partSectionSet = gettingByteLine(4);
            position += 36;
        }
        position -= 36;
        position += 12; //Отправляемся в sh_offset
        text_sh_offset = gettingByteLine(4);
        text_sh_size = gettingByteLine(4);
        completingTextAndSections();
        if (!hasOutFile) outPrint();
        else outFilePrint(args[1]);
    }

    private static int gettingOffset() {
        int temp_type_of_section = 0;
        int tempOffset = 0;
        while (temp_type_of_section != 3 && temp_type_of_section != -1) {
            position += 4;
            temp_type_of_section = gettingByteLine(4);
            position += 8;
            tempOffset = gettingByteLine(4);
            position += 20;
        }
        if (temp_type_of_section == -1) return sht_strtab_offset;
        return tempOffset;
    }

    private static void outFilePrint(String nameOfFile) {
        try (PrintStream writer = new PrintStream(nameOfFile)) {
            writer.print("Symbol Table (.symtab)\n");
            writer.printf("%-8s%-20s%-11s%-11s%-11s%-11s%-11s%-11s%n", "Symbol", "Value", "Size", "Type", "Bind", "Vis"
                    , "Index", "Name");
            for (int i = 0; i < symTable.length; i++) {
                writer.printf("%-8s%-20s%-11s%-11s%-11s%-11s%-11s%-11s%n", "[" + symTable[i][0] + "]", "0x" + symTable[i][1], symTable[i][2]
                        , symTable[i][3], symTable[i][4], symTable[i][5], symTable[i][6], symTable[i][7]);
            }
            for (int i = 0; i < 4; i++) System.out.println();
            for (int i = 0; i < text_sh_size / 4; i++) {
                String[] s = new String[7];
                assembler_commands[i][0] = "0x" + assembler_commands[i][0] + ":";
                s[0] = assembler_commands[i][0];
                s[1] = "";
                if (assembler_commands[i][1] != null) s[1] = assembler_commands[i][1];
                for (int j = 2; j < 6; j++) {
                    if (assembler_commands[i][j] == null) assembler_commands[i][j] = "";
                    if (assembler_commands[i][j].equals("a0")) assembler_commands[i][j] = "zero";
                    s[j] = assembler_commands[i][j];
                }
                writer.printf("%-15s%-27s%-8s%-8s%-8s%-8s%n", s[0], s[1], s[2], s[3], s[4], s[5]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outPrint() {
        System.out.println("Symbol Table (.symtab)");
        System.out.printf("%-8s%-20s%-11s%-11s%-11s%-11s%-11s%-11s%n", "Symbol", "Value", "Size", "Type", "Bind", "Vis"
                , "Index", "Name");
        for (int i = 0; i < symTable.length; i++) {
            System.out.printf("%-8s%-20s%-11s%-11s%-11s%-11s%-11s%-11s%n", "[" + symTable[i][0] + "]", "0x" + symTable[i][1], symTable[i][2]
                    , symTable[i][3], symTable[i][4], symTable[i][5], symTable[i][6], symTable[i][7]);
        }
        for (int i = 0; i < 4; i++) System.out.println();
        for (int i = 0; i < text_sh_size / 4; i++) {
            String[] s = new String[7];
            assembler_commands[i][0] = "0x" + assembler_commands[i][0] + ":";
            s[0] = assembler_commands[i][0];
            s[1] = "";
            if (assembler_commands[i][1] != null) s[1] = assembler_commands[i][1];
            for (int j = 2; j < 6; j++) {
                if (assembler_commands[i][j] == null) assembler_commands[i][j] = "";
                if (assembler_commands[i][j].equals("a0")) assembler_commands[i][j] = "zero";
                s[j] = assembler_commands[i][j];
            }
            System.out.printf("%-15s%-27s%-8s%-8s%-8s%-8s%n", s[0], s[1], s[2], s[3], s[4], s[5]);
        }
    }

    private static void completingTextAndSections() {
        int counts = text_sh_size / 4;//Оазмер команды 4 байта
        assembler_commands = new String[counts][6]; //0xX code, mark, FUNC, x1, x2, x3, comm
        position = text_sh_offset;//переходим в сдвиг
        for (int i = 0; i < counts; i++) {
            StringBuilder command_code = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                command_code.insert(0, Integer.toBinaryString(tableOfElfFile[position++]));
                while (command_code.length() < 8 * (j + 1)) command_code.insert(0, "0");
            }
            baseInstructionSetRV32I(command_code.toString(), i);
            StringBuilder outOfNumCommand = new StringBuilder(Integer.toHexString(i * 4));
            while (outOfNumCommand.length() < 8) outOfNumCommand.insert(0, "0");
            assembler_commands[i][0] = outOfNumCommand.toString();
            if (assembler_commands[i][2].equals("JAL") || assembler_commands[i][2].equals("JALR") || assembler_commands[i][2].equals("BGE")) {
                assembler_commands[i][1] = "<LOC_" + assembler_commands[i][0] + ">";
            }//метки
            if (checkingInMapOfNameSymTab(assembler_commands[i][0])) {//обозначение меток из symTable
                assembler_commands[i][1] = id_of_names.get(assembler_commands[i][0]);
            }
        }


    }

    private static void baseInstructionSetRV32I(String command_code, int numOfOperation) {
        String opcode = command_code.substring(25, 32);
        String func3 = command_code.substring(17, 20);
        if (opcode.equals("0110111")) {
            assembler_commands[numOfOperation][2] = "LUI";
            uDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010111")) {
            assembler_commands[numOfOperation][2] = "AUIPC";
            uDecoding(command_code, numOfOperation);
        } else if (opcode.equals("1101111")) {
            assembler_commands[numOfOperation][2] = "JAL";
            jDecoding(command_code, numOfOperation);
        } else if (opcode.equals("1100111")) {
            assembler_commands[numOfOperation][2] = "JALR";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("1100011") && func3.equals("000")) {
            assembler_commands[numOfOperation][2] = "BEQ";
            bDecoding(command_code, numOfOperation);
        } else if (opcode.equals("1100011") && func3.equals("001")) {
            assembler_commands[numOfOperation][2] = "BNE";
            bDecoding(command_code, numOfOperation);
        } else if (opcode.equals("1100011") && func3.equals("100")) {
            assembler_commands[numOfOperation][2] = "BLT";
            bDecoding(command_code, numOfOperation);
        } else if (opcode.equals("1100011") && func3.equals("101")) {
            assembler_commands[numOfOperation][2] = "BGE";
            bDecoding(command_code, numOfOperation);
        } else if (opcode.equals("1100011") && func3.equals("110")) {
            assembler_commands[numOfOperation][2] = "BLTU";
            uDecoding(command_code, numOfOperation);
        } else if (opcode.equals("1100011") && func3.equals("111")) {
            assembler_commands[numOfOperation][2] = "BGEU";
            uDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0000011") && func3.equals("000")) {
            assembler_commands[numOfOperation][2] = "LB";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0000011") && func3.equals("001")) {
            assembler_commands[numOfOperation][2] = "LH";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0000011") && func3.equals("010")) {
            assembler_commands[numOfOperation][2] = "LW";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0000011") && func3.equals("100")) {
            assembler_commands[numOfOperation][2] = "LBU";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0000011") && func3.equals("101")) {
            assembler_commands[numOfOperation][2] = "LHU";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0100011") && func3.equals("000")) {
            assembler_commands[numOfOperation][2] = "SB";
            sDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0100011") && func3.equals("001")) {
            assembler_commands[numOfOperation][2] = "SH";
            sDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0100011") && func3.equals("010")) {
            assembler_commands[numOfOperation][2] = "SW";
            sDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("000")) {///////////
            assembler_commands[numOfOperation][2] = "ADDI";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("010")) {
            assembler_commands[numOfOperation][2] = "SLTI";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("011")) {
            assembler_commands[numOfOperation][2] = "SLTIU";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("100")) {
            assembler_commands[numOfOperation][2] = "XORI";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("110")) {
            assembler_commands[numOfOperation][2] = "ORI";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("111")) {
            assembler_commands[numOfOperation][2] = "ANDI";
            iDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("001")) {
            assembler_commands[numOfOperation][2] = "SLLI";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("101") && command_code.charAt(1) == '0') {
            assembler_commands[numOfOperation][2] = "SRLI";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0010011") && func3.equals("101") && command_code.charAt(1) == '1') {
            assembler_commands[numOfOperation][2] = "SRAI";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("000") && command_code.charAt(1) == '0' && command_code.charAt(6) != '1') {////////
            assembler_commands[numOfOperation][2] = "ADD";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("000") && command_code.charAt(1) == '1' && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "SUB";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("001") && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "SLL";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("010") && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "SLT";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("011") && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "SLTU";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("100") && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "XOR";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("101") && command_code.charAt(1) == '0' && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "SRL";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("101") && command_code.charAt(1) == '1' && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "SRA";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("110") && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "OR";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("111") && command_code.charAt(6) != '1') {
            assembler_commands[numOfOperation][2] = "AND";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0001111")) {
            assembler_commands[numOfOperation][2] = "FENCE";
        } else if (opcode.equals("1110011") && command_code.charAt(11) == '0') {
            assembler_commands[numOfOperation][2] = "ECALL";
        } else if (opcode.equals("1110011") && command_code.charAt(11) == '1') {
            assembler_commands[numOfOperation][2] = "EBREAK";
        } else if (opcode.equals("0110011") && func3.equals("000") && command_code.charAt(6) == '1') {////////
            assembler_commands[numOfOperation][2] = "MUL";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("001") && command_code.charAt(6) == '1') {////////
            assembler_commands[numOfOperation][2] = "MULH";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("010") && command_code.charAt(6) == '1') {////////
            assembler_commands[numOfOperation][2] = "MULHSU";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("011") && command_code.charAt(6) == '1') {////////
            assembler_commands[numOfOperation][2] = "MULHU";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("100") && command_code.charAt(6) == '1') {////////
            assembler_commands[numOfOperation][2] = "DIV";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("101") && command_code.charAt(6) == '1') {////////
            assembler_commands[numOfOperation][2] = "DIVU";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("110") && command_code.charAt(6) == '1') {////////
            assembler_commands[numOfOperation][2] = "REM";
            rDecoding(command_code, numOfOperation);
        } else if (opcode.equals("0110011") && func3.equals("111") && command_code.charAt(6) == '1') {////////
            assembler_commands[numOfOperation][2] = "REMU";
            rDecoding(command_code, numOfOperation);
        } else {
            assembler_commands[numOfOperation][2] = "UNKNOWN COMMAND";
        }
    }

    private static void downloadingMaps() {
        String[] typeArrKey = new String[]{"0", "1", "2", "3", "4", "5", "a", "c", "d", "f"};
        String[] typeArrValue = new String[]{"NOTYPE", "OBJECT", "FUNC", "SECTION", "FILE", "COMMON", "LOOS", "HIOS"
                , "LOPROC", "HIPROC"};
        String[] bindArrKey = new String[]{"0", "1", "2", "a", "c", "d", "f"};
        String[] bindArrValue = new String[]{"LOCAL", "GLOBAL", "WEAK", "LOOS", "HIOS", "LOPROC", "HIPROC"};
        String[] visArrKey = new String[]{"0", "1", "2", "3"};
        String[] visArrValue = new String[]{"DEFAULT", "INTERNAL", "HIDDEN", "PROTECTED"};
        for (int i = 0; i < typeArrKey.length; i++) {
            symbol_types.put(typeArrKey[i], typeArrValue[i]);
        }
        for (int i = 0; i < bindArrKey.length; i++) {
            symbol_binds.put(bindArrKey[i], bindArrValue[i]);
        }
        for (int i = 0; i < visArrKey.length; i++) {
            symbol_visibility.put(visArrKey[i], visArrValue[i]);
        }
    }

    private static void gettingSymTable() {
        int countsOfElements = sh_size / sh_entsize;
        position = sh_offset;
        symTable = new String[countsOfElements][8]; //num, value, size, type, bind, visibility, index, name
        for (int i = 0; i < countsOfElements; i++) {
            symTable[i][0] = Integer.toString(i);
            int tempName = gettingByteLine(4);  //Смещение относительно таблдицы строк для п
            // олучение нимени файлов и т.п
            symTable[i][1] = Integer.toHexString(gettingByteLine(4));//value
            symTable[i][2] = Integer.toString(gettingByteLine(4));//size
            StringBuilder struct_sym_info = new StringBuilder(Integer.toHexString(tableOfElfFile[position++]));
            if (struct_sym_info.length() == 1) {
                struct_sym_info.insert(0, '0');
            }
            symTable[i][3] = symbol_types.get(Character.toString(struct_sym_info.charAt(1)));//type
            symTable[i][4] = symbol_binds.get(Character.toString(struct_sym_info.charAt(0)));//bind
            symTable[i][5] = symbol_visibility.get(Integer.toString(gettingByteLine(1)));
            symTable[i][6] = Integer.toString(gettingByteLine(2));
            if (symTable[i][3].equals("FILE")) {
                symTable[i][6] = "ABS";//index
            } else if (symTable[i][6].equals("0")) {
                symTable[i][6] = "UNDEF";
            }
            position = tempName + sht_strtab_offset;
            StringBuilder tempOutName = new StringBuilder();
            int tempNum = 0;
            do {
                tempNum = tableOfElfFile[position];
                if (tableOfElfFile[position] != -1) {
                    tempOutName.append((char) tableOfElfFile[position++]);
                } else {
                    break;
                }
            } while (tempNum != 0);
            tempOutName.deleteCharAt(tempOutName.length() - 1);
            symTable[i][7] = tempOutName.toString();
            position = sh_offset + sh_entsize * (i + 1);
        }
        gettingNamesOfSymTab(); //Занесение имен и значений
    }

    private static boolean checkingInMapOfNameSymTab(String line) {
        for (Map.Entry<String, String> entry : id_of_names.entrySet()) {
            if (line.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    private static void gettingNamesOfSymTab() {
        for (int i = symTable.length - 1; i > 0; i--) {
            if (symTable[i][7] != null) {
                StringBuilder tempValue = new StringBuilder(symTable[i][1]);
                while (tempValue.length() < 8) tempValue.insert(0, "0");
                id_of_names.put(tempValue.toString(), "<" + symTable[i][7] + ">");
            }
        }
    }

    private static int gettingByteLine(int countsByteReadingElement) {
        try {

            StringBuilder byteHex = new StringBuilder();
            for (int i = 0; i < countsByteReadingElement; i++) {
                String tempChar = Integer.toHexString(tableOfElfFile[position + i]);
                byteHex.insert(0, tempChar);
                if (tempChar.length() == 1) {
                    byteHex.insert(0, "0");
                }
            }
            position += countsByteReadingElement;
            return Integer.parseInt(byteHex.toString(), 16);
        } catch (Exception e) {
            return -1;
        }

    }

    private static String getImmStringToInt(String immString) {
        if (immString.charAt(0) == '0') {
            return Integer.toString(Integer.parseInt(immString, 2));
        } else {
            int endRezult = 0;
            for (int i = immString.length() - 1; i > -1; i--) {
                if (immString.charAt(i) == '0') endRezult += Math.pow(2, immString.length() - i - 1);
            }

            return Integer.toString(-endRezult - 1);
        }
    }

    private static void gettingInformationAboutSymTable() throws IOException {
        position = e_shoff;
        int sh_type = -1;
        do {
            position += 4;
            sh_type = gettingByteLine(4);
            position += 32;
        } while (sh_type != 2); //SHT_SYMTAB	ЗНАЧЕНИЕ - 2	Секция содержит таблицу символов.
        position -= 32;
        position += 8;
        sh_offset = gettingByteLine(4);
        sh_size = gettingByteLine(4);
        position += 12;
        sh_entsize = gettingByteLine(4);

    }

    private static boolean thisIsElfFile() {
        while (position < tableOfElfFile.length) {
            StringBuilder checkingString = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                if (position + i < tableOfElfFile.length) checkingString.append((char) tableOfElfFile[position + i]);
            }
            if (checkingString.toString().equals("\u007FELF")) {
                position += 4;
                return true;
            }
        }

        return false;
    }

    private static int[] readElfFile(String name) throws IOException {
        try (FileInputStream fos = new FileInputStream(name)) {
            int i;
            List<Integer> tempArr = new ArrayList<>();
            while ((i = fos.read()) != -1) {
                tempArr.add(i);
            }
            int[] byteArr = new int[tempArr.size()];
            for (int j = 0; j < byteArr.length; j++) {
                byteArr[j] = tempArr.get(j);
            }
            return byteArr;
        } catch (FileNotFoundException e) {
            System.out.println("The file with this name was not found.");
        }
        return null;
    }

    private static void rDecoding(String code, int num) {  //
        int rd = Integer.parseInt(code.substring(20, 25), 2);
        int rs1 = Integer.parseInt(code.substring(12, 17), 2);
        int rs2 = Integer.parseInt(code.substring(7, 12), 2);
        assembler_commands[num][3] = "a" + rd;
        assembler_commands[num][4] = "a" + rs1;
        assembler_commands[num][5] = "a" + rs2;

    }

    private static void iDecoding(String code, int num) {
        int rd = Integer.parseInt(code.substring(20, 25), 2);
        int rs1 = Integer.parseInt(code.substring(12, 17), 2);
        String immString = code.substring(0, 12);
        assembler_commands[num][3] = "a" + rd;
        assembler_commands[num][4] = "a" + rs1;
        assembler_commands[num][5] = getImmStringToInt(immString);
    }

    private static void sDecoding(String code, int num) {
        int rs2 = Integer.parseInt(code.substring(7, 12), 2);
        int rs1 = Integer.parseInt(code.substring(12, 17), 2);
        String immString = code.substring(1, 7) + code.substring(20, 25);
        assembler_commands[num][3] = "a" + rs1;
        assembler_commands[num][4] = "a" + rs2;
        assembler_commands[num][5] = getImmStringToInt(immString);
    }

    private static void bDecoding(String code, int num) {
        int rs2 = Integer.parseInt(code.substring(7, 12), 2);
        int rs1 = Integer.parseInt(code.substring(12, 17), 2);
        String immString = Character.toString(code.charAt(0)) + Character.toString(code.charAt(24)) + code.substring(1, 7) + code.substring(20, 24) + "0";
        assembler_commands[num][3] = "a" + rs1;
        assembler_commands[num][4] = "a" + rs2;
        assembler_commands[num][5] = getImmStringToInt(immString);
    }

    private static void uDecoding(String code, int num) {
        int rd = Integer.parseInt(code.substring(20, 25), 2);
        StringBuilder immStringBuilder = new StringBuilder(code.substring(0, 20));
        while (immStringBuilder.length() < 32) immStringBuilder.append("0");
        assembler_commands[num][3] = "a" + rd;
        assembler_commands[num][4] = getImmStringToInt(immStringBuilder.toString());
    }

    private static void jDecoding(String code, int num) {
        int rd = Integer.parseInt(code.substring(20, 25), 2);
        String immString = code.substring(12, 20) + Character.toString(code.charAt(11)) + code.substring(1, 7) + code.substring(7, 11) + "0";
        assembler_commands[num][3] = "a" + rd;
        assembler_commands[num][4] = getImmStringToInt(immString);
    }

}

