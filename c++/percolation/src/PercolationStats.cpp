#include "PercolationStats.h"

#include <Percolation.h>
#include <cmath>
#include <iostream>
#include <random>

/*This 'random_struct' was taken from 'ICA checks' */

struct random_struct
{
    random_struct(const size_t max_value)
        : to(max_value)
        , m_rand_engine(std::random_device{}()) // construct random device and call to get seed for random engine construction
    {
    }
    size_t get_rand()
    {
        std::uniform_int_distribution distribution(from, to); // distribution construction is quite cheap
        return distribution(m_rand_engine);
    }

private:
    static constexpr size_t from = 0;
    const size_t to;
    std::mt19937_64 m_rand_engine;

    /*
     * Вообще mutable делает переменную изменяемой, вчастности в функциях с модификатором const. Сейчас модификатор mutable убран из-за того, что
     * модификатор const в функции execute() был убран.
     * */
};

PercolationStats::PercolationStats(const size_t dimension, const size_t trials)
    : dimension_of_table(dimension)
    , counts_of_trials(trials)
    , counts_of_cells(dimension * dimension)
{
    execute();
}

double PercolationStats::get_mean() const
{
    return mean;
}

double PercolationStats::get_standard_deviation() const
{
    return standard_deviation;
}

double PercolationStats::get_confidence_low() const
{
    return mean - (1.96 * standard_deviation / std::sqrt(counts_of_trials));
}

double PercolationStats::get_confidence_high() const
{
    return mean + (1.96 * standard_deviation / std::sqrt(counts_of_trials));
}

void PercolationStats::execute()
{
    std::vector<double> counts_of_percolation(counts_of_trials);
    random_struct random(dimension_of_table - 1);
    for (size_t i = 0; i < counts_of_trials; ++i) {
        Percolation percolation(dimension_of_table);
        while (!percolation.has_percolation()) {
            size_t row = random.get_rand();
            size_t column = random.get_rand();
            percolation.open(row, column);
        }
        size_t counts_of_open_cells = percolation.get_numbet_of_open_cells();
        counts_of_percolation[i] = static_cast<double>(counts_of_open_cells) / (counts_of_cells);
    }

    for (size_t i = 0; i < counts_of_trials; ++i) {
        mean += counts_of_percolation[i];
    }
    mean /= counts_of_trials;

    for (size_t i = 0; i < counts_of_trials; ++i) {
        standard_deviation += std::pow(counts_of_percolation[i] - mean, 2);
    }
    standard_deviation = std::sqrt(standard_deviation / (counts_of_trials - 1));
}