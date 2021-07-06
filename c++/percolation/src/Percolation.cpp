#include "Percolation.h"

#include <vector>

Percolation::Percolation(size_t dimension)
    : size(dimension)
    , table(dimension, std::vector<Cell>(dimension, Cell::Closed))
{
}

void Percolation::open(const size_t row, const size_t column)
{
    if (!is_open(row, column)) {
        table[row][column] = Cell::Open;
        if (row == 0 || is_full(row - 1, column) || (row != size - 1 && is_full(row + 1, column)) || (column != 0 && is_full(row, column - 1)) || (column != size - 1 && is_full(row, column + 1))) {
            rewrite_full_cells(row, column);
        }
    }
}

bool Percolation::is_open(const size_t row, const size_t column) const
{
    return table[row][column] == Cell::Open || table[row][column] == Cell::Full;
}

bool Percolation::is_full(const size_t row, const size_t column) const
{
    return table[row][column] == Cell::Full;
}

size_t Percolation::get_numbet_of_open_cells() const
{
    size_t numbet_of_open_cells = 0;
    for (size_t i = 0; i < size; ++i) {
        for (size_t j = 0; j < size; ++j) {
            if (is_open(i, j)) {
                numbet_of_open_cells++;
            }
        }
    }

    return numbet_of_open_cells;
}

bool Percolation::has_percolation() const
{
    for (size_t i = 0; i < size; ++i) {
        if (is_full(size - 1, i)) {
            return true;
        }
    }
    return false;
}

void Percolation::rewrite_full_cells(size_t row, size_t column)
{
    table[row][column] = Cell::Full;
    if (row != 0 && table[row - 1][column] == Cell::Open)
        rewrite_full_cells(row - 1, column);
    if (row != size - 1 && table[row + 1][column] == Cell::Open)
        rewrite_full_cells(row + 1, column);
    if (column != 0 && table[row][column - 1] == Cell::Open)
        rewrite_full_cells(row, column - 1);
    if (column != size - 1 && table[row][column + 1] == Cell::Open)
        rewrite_full_cells(row, column + 1);
}
