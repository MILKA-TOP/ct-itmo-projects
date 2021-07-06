#pragma once

#include <map>
#include <set>
#include <string_view>
#include <vector>

/**
 * Всё решение этой задачи свобидтся к тому, что мы делим наши фрагнменты генома, далее
 * разделяем их на более маленькие подстроки, из которых составляем граф. Искомый геном -
 * это путь Эйлера составленного графа. Для его нахождения был использован следующий
 * алгоритм: "Hierholzer’s Algorithm".
 * */

namespace genome {

/**
 * Класс, хранящий в себе методы и данные, связанные с геномом и эйлеровым путём
 * */
class Genome_Euler_Path
{
public:
    /**
    * Разделяет фрагмент генома на ряд подстрок длины (k + 1)
    * */
    void splitting_genome(size_t k, std::string_view genome_part);

    /**
     * Берёт подстроку, полученную в функции 'splitting_genome' и делает из неё
     * две вершины направленного графа
     * */
    void make_genome_edge(size_t k, std::string_view genome_fragment);

    /**
     * Удаляет ребро между двумя вершинами направленного графа
     * */
    void remove_genome_edge(std::string_view current_value);

    /**
    * Создаёт направленный граф, где объединяет все ребра, полученные в функции
    * 'make_genome_edge'
    * */
    void make_euler_graph();

    /**
     * Возвращает вектор, элементы которого являются путём Эйлера. Его нахождение происходит
     * благодаря Алгоритму Хирхольцера.
     * */
    std::vector<std::string_view> get_euler_path(std::string_view value);

    /**
    * Возвращает вершину, из которой будет происходить начало получения пути Эйлера
    * */
    std::string_view get_first_node();

private:
    std::set<std::string_view> nodes;
    std::vector<std::pair<std::string_view, std::string_view>> edges;
    std::map<std::string_view, std::vector<std::string_view>> euler_graph;
    std::map<std::string_view, int> degree;
};

/**
 * Возвращает геном, собранный из фрагментов
 * */
std::string assembly(size_t k, const std::vector<std::string> & reads);

/**
 * Возвращает геном, полученный при обработке пути Эйлера из функции 'get_euler_path'
 * */
std::string get_genome_assembly(const std::vector<std::string_view> & euler_path);
} // namespace genome
