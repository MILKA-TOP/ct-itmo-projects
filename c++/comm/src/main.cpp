#include <algorithm>
#include <cstring>
#include <fstream>
#include <iostream>
#include <string>
#include <vector>


class Container {
    std::vector<std::string> lines;
public:
    void add(const std::string &line) {
        lines.push_back(line);
    }

    std::string get(const size_t i) {
        return (i < size()) ? lines.at(i) : std::string();
    }

    std::size_t size() {
        return lines.size();
    }

    void both(Container &vector_input_first, Container &vector_input_second) {
        std::vector<std::string> first_vector = vector_input_first.get_vector();
        std::vector<std::string> second_vector = vector_input_second.get_vector();
        std::set_intersection(first_vector.begin(),
                              first_vector.end(),
                              second_vector.begin(),
                              second_vector.end(),
                              std::inserter(lines, lines.begin()));
    }

    void unique(Container &vector_input_first, Container &vector_input_second) {
        std::vector<std::string> first_vector = vector_input_first.get_vector();
        std::vector<std::string> second_vector = vector_input_second.get_vector();
        std::set_difference(first_vector.begin(),
                            first_vector.end(),
                            second_vector.begin(),
                            second_vector.end(),
                            std::inserter(lines, lines.begin()));
    }


private:
    std::vector<std::string> get_vector() {
        return lines;
    }
};

using Lines = Container;

void print_tabulation(const size_t start_row, const size_t num_of_row) {
    for (size_t i = 0; i < (num_of_row - start_row); ++i) {
        std::cout << "\t";
    }
}

void
print_out(Lines &c1, Lines &c2, Lines &c3, const size_t count_of_rows, const size_t start_row,
          const bool column_bool[]) {

    size_t i = 0, j = 0, k = 0;
    size_t row_num_output = 0;
    std::string output_line;
    std::string line_first_row = c1.get(i);
    std::string line_second_row = c2.get(j);
    std::string line_third_row = c3.get(k);
    while ((i + j + k) < (c1.size() + c2.size() + c3.size())) {
        if (column_bool[0] && !line_first_row.empty() &&
            (line_first_row <= line_second_row || line_second_row.empty()) &&
            (line_first_row <= line_third_row || line_third_row.empty())) {
            row_num_output = 1;
            output_line = line_first_row;
            line_first_row = c1.get(++i);
        } else if (column_bool[1] && !line_second_row.empty() &&
                   (line_second_row <= line_first_row || line_first_row.empty()) &&
                   (line_second_row <= line_third_row || line_third_row.empty())) {
            row_num_output = 2;
            output_line = line_second_row;
            line_second_row = c2.get(++j);
        } else if (column_bool[2] && !line_third_row.empty() &&
                   (line_third_row <= line_first_row || line_first_row.empty()) &&
                   (line_third_row <= line_second_row || line_second_row.empty())) {
            row_num_output = 3;
            output_line = line_third_row;
            line_third_row = c3.get(++k);
        } else {
            if (column_bool[2] && c3.size() != 0 && k < c3.size()) {
                output_line = line_third_row;
                line_third_row = c3.get(++k);
                row_num_output = 3;
            } else if (column_bool[1] && c2.size() != 0 && j < c2.size()) {
                output_line = line_second_row;
                line_second_row = c2.get(++j);
                row_num_output = 2;
            } else if (column_bool[0] && c1.size() != 0 && i < c1.size()) {
                output_line = line_first_row;
                line_first_row = c1.get(++i);
                row_num_output = 1;
            }

        }
        if (count_of_rows == 2 && row_num_output == 3 && start_row == 1) row_num_output = 2;
        print_tabulation(start_row, row_num_output);
        std::cout << output_line << "\n";
    }
}


void
sort_stream(std::istream &input_1, std::istream &input_2, const bool column_bool[]) {
    Lines file_first;
    Lines file_second;
    Lines row_out_first;
    Lines row_out_second;
    Lines row_out_third;
    std::string line;
    size_t count_of_rows = 0;
    size_t minimum_row = 0;

    while (std::getline(input_1, line)) {
        file_first.add(line);
    }
    while (std::getline(input_2, line)) {
        file_second.add(line);
    }
    if (column_bool[2]) {
        minimum_row = 3;
        row_out_third.both(file_first, file_second);
        count_of_rows++;
    }
    if (column_bool[1]) {
        minimum_row = 2;
        row_out_second.unique(file_second, file_first);
        count_of_rows++;
    }
    if (column_bool[0]) {
        minimum_row = 1;
        row_out_first.unique(file_first, file_second);
        count_of_rows++;
    }
    print_out(row_out_first, row_out_second, row_out_third, count_of_rows, minimum_row, column_bool);
}

int main(int argc, char **argv) {
    std::string file_names[2];
    bool column_bool[3] = {true, true, true};
    size_t counts_checked_filenames = 0;

    for (int i = 1; i < argc; ++i) {
        if (argv[i][0] == '-' && (std::strlen(argv[i]) != 1)) {
            const size_t len = std::strlen(argv[i]);
            for (size_t j = 1; j < len; ++j) {
                switch (argv[i][j]) {
                    case '1':
                        column_bool[0] = false;
                        break;
                    case '2':
                        column_bool[1] = false;
                        break;
                    case '3':
                        column_bool[2] = false;
                        break;
                }
            }

        } else {
            file_names[counts_checked_filenames] = argv[i];
            counts_checked_filenames++;
        }
    }

    /*
     * Checking if we have filename "-" in arguments and using standard input.
     */

    if (file_names[0].length() == 1 && file_names[0][0] == '-'
        && file_names[1].length() == 1 && file_names[1][0] == '-') {
        sort_stream(std::cin, std::cin, column_bool);
    } else if (file_names[0].length() == 1 && file_names[0][0] == '-') {
        std::ifstream f(file_names[0]);
        sort_stream(std::cin, f, column_bool);
    } else if (file_names[1].length() == 1 && file_names[1][0] == '-') {
        std::ifstream f(file_names[1]);
        sort_stream(f, std::cin, column_bool);
    } else {
        std::ifstream f(file_names[0]);
        std::ifstream s(file_names[1]);
        sort_stream(f, s, column_bool);
    }

    return 0;
}