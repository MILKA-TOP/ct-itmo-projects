#include "genome.h"

#include <algorithm>
#include <iostream>
#include <map>
#include <stack>

namespace genome {
void Genome_Euler_Path::remove_genome_edge(const std::string_view current_value)
{
    std::string_view side_value = euler_graph[current_value].back();
    euler_graph[current_value].pop_back();
    degree[current_value]--;
    degree[side_value]++;
}

void Genome_Euler_Path::make_genome_edge(const size_t k, const std::string_view genome_fragment)
{
    std::string_view first = genome_fragment.substr(0, k);
    std::string_view second = genome_fragment.substr(genome_fragment.size() - k);
    nodes.insert(first);
    nodes.insert(second);
    edges.push_back(std::make_pair(first, second));
    if (degree.count(first) == 0) {
        degree.insert(std::make_pair(first, 0));
    }
    if (degree.count(second) == 0) {
        degree.insert(std::make_pair(second, 0));
    }
    degree[first]++;
    degree[second]--;
}

void Genome_Euler_Path::splitting_genome(const size_t k, const std::string_view genome_part)
{
    for (int i = 0; i + k < genome_part.size(); ++i) {
        std::string_view line{genome_part.substr(i, k + 1)};
        make_genome_edge(k, line);
    }
}

void Genome_Euler_Path::make_euler_graph()
{
    for (const auto & now_edge : edges) {
        std::string_view first_node = now_edge.first;
        euler_graph[first_node].push_back(now_edge.second);
    }
}

std::vector<std::string_view> Genome_Euler_Path::get_euler_path(const std::string_view value)
{
    std::vector<std::string_view> euler_path_vector;
    std::stack<std::string_view> current_path;
    std::stack<std::string_view> euler_path_stack;
    current_path.push(value);
    while (!current_path.empty()) {
        std::string_view now_node = current_path.top();
        if (euler_graph[now_node].empty()) {
            euler_path_stack.push(now_node);
            current_path.pop();
        }
        else {
            current_path.push(euler_graph[now_node].back());
            remove_genome_edge(now_node);
        }
    }
    while (!euler_path_stack.empty()) {
        euler_path_vector.push_back(euler_path_stack.top());
        euler_path_stack.pop();
    }
    return euler_path_vector;
}

std::string_view Genome_Euler_Path::get_first_node()
{
    std::string_view start = *nodes.begin();
    int input_degree = 0, output_degree = 0;
    for (const auto & element : nodes) {
        if (degree[element] == -1 && input_degree == 0) {
            input_degree++;
        }
        else if (degree[element] == 1 && output_degree == 0) {
            output_degree++;
            start = element;
        }
    }
    return start;
}

std::string get_genome_assembly(const std::vector<std::string_view> & euler_path)
{
    std::string final_genome;
    if (euler_path.empty())
        return "";
    size_t size_genome_element = euler_path.front().size();
    final_genome = euler_path.front().substr(0, size_genome_element - 1);
    for (const auto & node : euler_path) {
        final_genome = final_genome + node[size_genome_element - 1];
    }
    return final_genome;
}

std::string assembly(const size_t k, const std::vector<std::string> & reads)
{
    if (k == 0 || reads.empty())
        return "";

    Genome_Euler_Path genome;
    for (const auto & genome_scan : reads) {
        genome.splitting_genome(k, genome_scan);
    }
    genome.make_euler_graph();
    std::string_view first_element = genome.get_first_node();
    std::string final_genome = get_genome_assembly(genome.get_euler_path(first_element));
    return final_genome;
}
} // namespace genome
