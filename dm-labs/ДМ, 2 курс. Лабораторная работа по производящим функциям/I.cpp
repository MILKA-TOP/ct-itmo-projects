#include <iostream>
#include <cmath>
#include <vector>
#include <set>
#include <queue>
#include <algorithm>
#include <numeric>

using namespace std;
long long maxMod = 104857601;


long long neercMethod(long long n, vector<long long> aVec, vector<long long> cVec, long long k) {
    while (n >= k) {
        for (long long i = k; i < 2 * k; ++i) {
            long long sum = 0;
            for (int j = 1; j < k + 1; ++j) {
                sum = (sum + ((-cVec[j] + maxMod) % maxMod * aVec[i - j] + maxMod) % maxMod + maxMod) % maxMod;
            }
            aVec[i] = sum;
        }
        vector<long long> multi(2 * cVec.size() + 1, 0);
        vector<long long> minC(cVec);
        for (int i = 0; i < minC.size(); ++i) {
            if (i % 2 != 0) minC[i] = (-minC[i] + maxMod) % maxMod;
        }

        for (int i = 0; i < multi.size(); i+=2) {
            for (int j = 0; j <= i; ++j) {
                multi[i] =
                        (multi[i] +
                         ((j <= k ? cVec[j] : 0) * ((i - j) <= k ? minC[i - j] : 0) + maxMod) %
                         maxMod + maxMod) % maxMod;
            }
        }
        int start = (n % 2 == 0) ? 0 : 1;
        for (int i = start; i < aVec.size(); i += 2) {
            aVec[i / 2] = aVec[i];
        }

        for (long long j = 0; j < cVec.size(); j++) {
            cVec[j] = multi[j * 2];
        }

        n /= 2;


    }
    return aVec[n];

}

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout.tie(NULL);

    long long k, n;
    cin >> k >> n;
    vector<long long> aVector(2 * k + 5, 0);
    vector<long long> cVector(k + 1, 1);
    for (int i = 0; i < k; ++i) {
        cin >> aVector[i];
    }
    for (int i = 1; i <= k; ++i) {
        long long temp;
        cin >> temp;
        cVector[i] = (-temp + maxMod) % maxMod;
    }

    cout << neercMethod(n - 1, aVector, cVector, k);

}