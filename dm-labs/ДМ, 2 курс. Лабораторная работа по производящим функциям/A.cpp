#include <iostream>
#include <cmath>
#include <vector>
#include <set>
#include <queue>
#include <algorithm>

using namespace std;
long long maxMod = 998244353;

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    int n, m;
    cin >> n >> m;
    vector<long long> p(n + 1);
    vector<long long> q(1001, 0);
    vector<long long> q_t(1000);
    vector<long long> multi(n + m + 1, 0);
    vector<long long> multi_div(n + m + 1, 0);
    for (int i = 0; i < n + 1; ++i) {
        cin >> p[i];
    }
    for (int i = 0; i < m + 1; ++i) {
        cin >> q[i];
    }
    cout << max(n, m) << endl;
    for (int i = 0; i < max(n, m) + 1; ++i) {
        if (i < p.size() && i < q.size()) cout << (p[i] + q[i]) % maxMod << " ";
        else if (i < p.size()) cout << p[i] % maxMod << " ";
        else if (i < q.size()) cout << q[i] % maxMod << " ";
    }
    cout << endl << n + m << endl;
    for (int i = 0; i < p.size(); ++i) {
        for (int j = 0; j < m + 1; ++j) {
            multi[i + j] = (multi[i + j] + (p[i] * q[j] + maxMod) % maxMod + maxMod) % maxMod;
        }
    }
    for (size_t elem: multi) {
        cout << elem % maxMod << " ";
    }
    cout << endl;
    for (int i = 0; i < 1000; ++i) {
        long long sum = 0;
        for (int j = 1; j <= i; ++j) {
            sum = (sum + (q[j] * q_t[i - j] + maxMod) % maxMod + maxMod) % maxMod;
        }
        long long p_val = (i < p.size()) ? p[i] : 0;
        q_t[i] = (((-sum + p_val + maxMod) % maxMod) / q[0] + maxMod) % maxMod;
    }


    for (long long i: q_t) {
        cout << i << " ";
    }
    cout << endl;


}