#include <iostream>
#include <cmath>
#include <vector>
#include <set>
#include <queue>
#include <algorithm>

using namespace std;
long long maxMod = 1000000000 + 7;

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout.tie(NULL);
    int k, m;
    cin >> k >> m;
    vector<long long> c(k, 0);
    vector<long long> res_t(m + 1, 0);
    vector<long long> counter_t(m + 1, 0);
    res_t[0] = 1;
    counter_t[0] = 1;
    vector<long long> temp(m + 1, 0);
    for (int i = 0; i < k; ++i) cin >> c[i];

    for (int i = 1; i <= m; ++i) {
        for (int j = 0; j < k; ++j) res_t[i] = (res_t[i] + ((i >= c[j]) ? counter_t[i - c[j]] : 0) + maxMod) % maxMod;

        // Multiply
        for (int j = 0; j <= i; ++j) {
            counter_t[i] = (counter_t[i] + (res_t[j] * res_t[i - j] + maxMod) % maxMod + maxMod) % maxMod;
        }
    }

    for (int i = 1; i < m + 1; ++i) {
        cout << res_t[i] << " ";
    }


}