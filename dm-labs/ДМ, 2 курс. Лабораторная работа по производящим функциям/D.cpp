#include <iostream>
#include <cmath>
#include <vector>
#include <set>
#include <queue>
#include <algorithm>
#include <numeric>

using namespace std;
vector<vector<long long>> combination(15, vector<long long>(15, -1));

long long gcd(long long a, long long b) {
    while (b != 0) {
        a %= b;
        swap(a, b);
    }
    return a;
}

long long getComb(long long k, long long n) {
    if (combination[k][n] != -1) return combination[k][n];
    if (k == 0 || k == n) {
        combination[k][n] = 1;
        return 1;
    }
    combination[k][n] = getComb(k - 1, n - 1) * n / k;
    return combination[k][n];
}

vector<long long> plusOperation(vector<long long> A, vector<long long> B) {
    long long nodBefore = gcd(A[1], B[1]);
    vector<long long> res = {B[1] / nodBefore * A[0] + A[1] / nodBefore * B[0], A[1] / nodBefore * B[1]};
    long long nod = gcd(abs(res[0]), abs(res[1]));
    res[0] /= abs(nod);
    res[1] /= abs(nod);

    if (res[1] < 0) {
        res[1] = -res[1];
        res[0] = -res[0];
    }
    return res;
}

vector<long long> makeGood(vector<long long> res) {
    long long nod = gcd(abs(res[0]), abs(res[1]));
    res[0] /= abs(nod);
    res[1] /= abs(nod);

    if (res[1] < 0) {
        res[1] = -res[1];
        res[0] = -res[0];
    }
    return res;
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
    int r, k;
    cin >> r >> k;

    vector<long long> p_list(k + 1, 0);
    for (int i = 0; i < k + 1; ++i) {
        cin >> p_list[i];
    }
    long long rMaxPow = 1;
    long long maxFact = 1;
    vector<long long> rPowVector(k + 1, 1);
    vector<long long> factVector(k + 1, 1);
    for (int i = 1; i <= k; ++i) {
        rMaxPow *= -r;
        maxFact *= i;
        rPowVector[i] = rMaxPow;
        factVector[i] = maxFact;
    }
    vector<vector<long long>> abc_vector(k + 1, vector<long long>(2, 1));
    for (int i = 0; i < k + 1; ++i) {
        vector<long long> upSum = {p_list[k - i], 1};
        for (int j = 0; j < i; ++j) {
            vector<long long> plusVector = abc_vector[j];

            plusVector[0] *= -factVector[k - j] * rPowVector[k - i];
            plusVector[1] *= factVector[k - i] * factVector[k - j - k + i];
            upSum = plusOperation(upSum, plusVector);

        }
        upSum[1] *= rPowVector[k - i];
        abc_vector[i] = upSum;
    }
/*
    for (vector<long long> vNow: abc_vector) {
        cout << vNow[0] << "/" << vNow[1] << " ";
    }
    cout << "\n================\n";*/
    vector<vector<long long>> nFactCoeffs = createCoeffs(12);
    vector<vector<long long>> outResult(k + 1, vector<long long>(2, 1));
    for (int i = 0; i < k + 1; ++i) {
        vector<long long> sumNow = {0, 1};
        for (int j = i; j < k + 1; ++j) {
            vector<long long> nowPlusB = abc_vector[j];
            nowPlusB[0] *= nFactCoeffs[j][i + 1];
            nowPlusB[1] *= nFactCoeffs[j][1];
            sumNow = plusOperation(sumNow, nowPlusB);
        }
        outResult[i] = sumNow;
    }
    for (const vector<long long> &vNow: outResult) {
        vector<long long> nowFinalRes = makeGood(vNow);
        cout << nowFinalRes[0] << "/" << nowFinalRes[1] << " ";
    }

}