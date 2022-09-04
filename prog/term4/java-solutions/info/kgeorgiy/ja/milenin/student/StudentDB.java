package info.kgeorgiy.ja.milenin.student;

import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentDB implements StudentQuery {

    private static final Function<Student, String> FULL_NAME =
            student -> student.getFirstName() + " " + student.getLastName();
    private static final Comparator<Student> NAME_COMP =
            Comparator.comparing(Student::getLastName)
                    .thenComparing(Student::getFirstName)
                    .reversed()
                    .thenComparing(Student::compareTo);

    private <T> List<T> getFunction(final Collection<Student> students, final Function<Student, T> getter) {
        return students.stream().map(getter).collect(Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(final List<Student> students) {
        return getFunction(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(final List<Student> students) {
        return getFunction(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(final List<Student> students) {
        return getFunction(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(final List<Student> students) {
        return getFunction(students, FULL_NAME);
    }

    @Override
    public Set<String> getDistinctFirstNames(final List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(final List<Student> students) {
        return students.stream().max(Student::compareTo).map(Student::getFirstName).orElse("");
    }

    public List<Student> sortStudentsBy(final Collection<Student> students, final Comparator<Student> comp) {
        return students.stream().sorted(comp).collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsById(final Collection<Student> students) {
        return sortStudentsBy(students, Student::compareTo);
    }

    @Override
    public List<Student> sortStudentsByName(final Collection<Student> students) {
        return sortStudentsBy(students, NAME_COMP);
    }

    private <T> List<Student> findStudentBy(final Collection<Student> students, T value, Function<Student, T> operation) {
        return students.stream().filter(student -> operation.apply(student).equals(value))
                .sorted(NAME_COMP)
                .collect(Collectors.toList());
    }

    // :NOTE: Дубли
    @Override
    public List<Student> findStudentsByFirstName(final Collection<Student> students, final String name) {
        return findStudentBy(students, name, Student::getFirstName);
        //return findStudentUtil(students.stream(), student -> student.getFirstName().equals(name));
    }

    @Override
    public List<Student> findStudentsByLastName(final Collection<Student> students, final String name) {
        return findStudentBy(students, name, Student::getLastName);
        //return findStudentUtil(students.stream(), student -> student.getLastName().equals(name));
    }

    @Override
    public List<Student> findStudentsByGroup(final Collection<Student> students, final GroupName group) {
        return findStudentBy(students, group, Student::getGroup);
        //return findStudentUtil(students.stream(), student -> student.getGroup().equals(group));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(final Collection<Student> students, final GroupName group) {
        return students.stream()
                .filter(student -> student.getGroup().equals(group))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }

}
