#include <vector>
#include <omp.h>
#include <fstream>
#include <iostream>

using namespace std;

float parallel_gauss_determinant(vector<vector<float>>& mat, int size);
int paralel_col_max(const vector<vector<float>>& mat, int numOfCol, int size);
int paralel_triangulation(vector<vector<float>>& mat, int size);

float parallel_gauss_determinant(vector<vector<float>>& mat, int size) {
    unsigned int swapCount = paralel_triangulation(mat, size);
    float det = 1;
    if (swapCount % 2 == 1) det = -1;
#pragma omp parallel for schedule(static)
    for (int i = 0; i < size; i++) {
        det *= mat[i][i];
    }
    return det;
}

int paralel_col_max(const vector<vector<float>>& mat, int numOfCol, int size) {
    float max = abs(mat[numOfCol][numOfCol]);
    int maxPos = numOfCol;
#pragma omp parallel for schedule(static)
    for (int i = numOfCol + 1; i < size; i++) {
        float element = abs(mat[i][numOfCol]);
        if (element > max) {
            max = element;
            maxPos = i;
        }
    }
    return maxPos;
}

int paralel_triangulation(vector<vector<float>>& mat, int size) {
    unsigned int swapCount = 0;
    for (int i = 0; i < size - 1; i++) {
        unsigned int imax = paralel_col_max(mat, i, size);
        if (i != imax) {
            swap(mat[i], mat[imax]);
            swapCount++;
        }
#pragma omp parallel for schedule(static)
        for (int j = i + 1; j < size; j++) {
            float mul = -mat[j][i] / mat[i][i];
#pragma omp parallel for schedule(static)
            for (int k = i; k < size; k++) {
                mat[j][k] += mat[i][k] * mul;
            }
        }
    }
    return swapCount;
}

int main(int argc, char** fileName) {
    int treads = atoi(fileName[1]);
    if (treads != 0) {
        omp_set_num_threads(treads);
    }
    ifstream in(fileName[2]);
    string temp = fileName[2];
    int lenght = temp.length();
    try {
        if (!in) {
            throw 0;
        }
        else if (lenght < 4 || fileName[2][lenght - 4] != '.' ||
            fileName[2][lenght - 1] != 't' || fileName[2][lenght - 2] != 'x' ||
            fileName[2][lenght - 3] != 't') {
            throw 0;
        }
        int n;
        in >> n;
        vector<vector<float>> matrix(n);
        for (int i = 0; i < n; i++) {
            matrix[i].resize(n);
            for (int j = 0; j < n; j++) {
                //matrix[i][j] = rand();
                in >> matrix[i][j];
            }
        }
        in.close();
        double start;
        double end;
        start = omp_get_wtime();
        printf("Determinant: %g\n", parallel_gauss_determinant(matrix, n));
        end = omp_get_wtime();
        matrix.clear();
        printf("\nTime (%i thread(s)): %f ms\n", treads, end - start);
    }
    catch (int i) {
        printf("Unknown or missing extension. Please use the .txt format");
        in.close();
    }
    return 0;
}