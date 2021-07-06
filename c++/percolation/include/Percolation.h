#pragma once

#include <stdio.h>
#include <vector>

enum class Cell
{
    Full,
    Open,
    Closed
};

struct Percolation
{
    /**
     * Size of the table
     */
    size_t size;

    /**
     * Table with "Closed", "Open" and "Full" cells
     */
    std::vector<std::vector<Cell>> table;

    /**
     * Construct a new Percolation object
     * @param dimension dimension of percolation table
     */
    Percolation(size_t dimension);

    /**
     * Opens the cell[row, column] if it's not opened already
     * @param row row index
     * @param column column index
     */
    void open(size_t row, size_t column);

    /**
     * Checks if cell[row, column] is open
     * @param row row index
     * @param column column index
     * @return true cell is open
     * @return false cell is blocked
     */
    bool is_open(size_t row, size_t column) const;

    /**
     * Checks if cell[row, column] is full
     * @param row row index
     * @param column column index
     * @return true cell is full
     * @return false cell is not full
     */
    bool is_full(size_t row, size_t column) const;

    /**
     * Gets number of open cells in table
     * @return number of open cells
     */
    size_t get_numbet_of_open_cells() const;

    /**
     * Checks if system percolates
     * @return true system percolates
     * @return false system doesn't percolate
     */
    bool has_percolation() const;

    /**
     * Turn open cell to full cell, if it has access to another full cell.
     * @param row row index
     * @param column column index
     */
    void rewrite_full_cells(size_t row, size_t column);
};