#include <iostream>
#include <cmath>
#include <vector>
#include <set>
#include <queue>
#include <algorithm>
#include <numeric>

using namespace std;

vector<long long> createCoeffs1(int size, int r) {
    vector<vector<long long>> result(size, vector<long long>());
    vector<long long> mainBracket(2, 1);
    mainBracket[0] = 0;
    result[0] = mainBracket;
    for (int i = 1; i < size; ++i) {
        mainBracket[0] = -r;
        vector<long long> result_add(result[i - 1].size() + 1, 0);
        for (int j = 0; j < result[i - 1].size(); ++j) {
            for (int l = 0; l < mainBracket.size(); ++l) {
                result_add[j + l] += result[i - 1][j] * mainBracket[l];
            }
        }
        result[i] = result_add;
    }
    return result[result.size() - 1];
    /*for (vector<long long> temp: result) {
        for (long long i: temp) {
            cout << i << " ";
        }
        cout << endl;
    }*/
}


vector<vector<long long>> createCoeffs(int size) {
    vector<vector<long long>> result(size, vector<long long>());
    vector<long long> mainBracket(2, 1);
    mainBracket[0] = 0;
    result[0] = mainBracket;
    for (int i = 1; i < size; ++i) {
        mainBracket[0] = i;
        vector<long long> result_add(result[i - 1].size() + 1, 0);
        for (int j = 0; j < result[i - 1].size(); ++j) {
            for (int l = 0; l < mainBracket.size(); ++l) {
                result_add[j + l] += result[i - 1][j] * mainBracket[l];
            }
        }
        result[i] = result_add;
    }
    return result;
    /*for (vector<long long> temp: result) {
        for (long long i: temp) {
            cout << i << " ";
        }
        cout << endl;
    }*/
}

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout.tie(NULL);
    int r, d;
    cin >> r >> d;
    vector<int> p_input(d + 1, 0);
    vector<long long> p_output(d + 1, 0);
    vector<long long> q_output(d + 1, 0);
    for (int i = 0; i < d + 1; ++i) {
        cin >> p_input[i];
    }

    long long rMaxPow = 1;
    long long maxFact = 1;
    vector<long long> rPowVector(d + 5, 1);
    vector<long long> factVector(d + 5, 1);
    for (int i = 1; i <= d + 3; ++i) {
        rMaxPow *= -r;
        maxFact *= i;
        rPowVector[i] = rMaxPow;
        factVector[i] = maxFact;
    }

    vector<vector<long long>> k_coeffs = createCoeffs(12);
    vector<long long> bigCoeffs(d + 1, 0);
    for (int i = d; i >= 0; --i) {
        long long someSum = p_input[i];
        for (int j = d; j > i; --j) {
            someSum -= bigCoeffs[j] * k_coeffs[j][i + 1] / k_coeffs[j][1];
        }
        bigCoeffs[i] = someSum * k_coeffs[i][1] / k_coeffs[i ][i + 1];
    }


    for (int i = 0; i < d + 1; ++i) {
        long long nowSum = 0;
        for (int j = 0; j <= i; ++j) {
            nowSum += bigCoeffs[j] * factVector[d - j] / (factVector[d - i] * factVector[i - j]);
        }
        p_output[i] = nowSum * rPowVector[d - i];
    }

    q_output = createCoeffs1(d + 2, r);
    std::reverse(p_output.begin(), p_output.end());
    std::reverse(q_output.begin(), q_output.end());
    while (!p_output.empty() && p_output[p_output.size() - 1] == 0) {
        p_output.pop_back();
    }
    cout << p_output.size() - 1 << endl;
    for (long long i: p_output) {
        cout << i << " ";
    }
    cout << endl << q_output.size() - 2 << endl;
    for (int i = 0; i < q_output.size() - 1; ++i) {
        cout << q_output[i] << " ";
    }
}