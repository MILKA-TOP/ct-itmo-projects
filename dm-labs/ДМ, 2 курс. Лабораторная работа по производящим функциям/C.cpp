#include <iostream>
#include <cmath>
#include <vector>
#include <set>
#include <queue>
#include <algorithm>

using namespace std;
long long maxMod = 1000000000000;

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    int k;
    cin >> k;
    vector<long long > a(k,0);
    vector<long long > c(k,0);
    vector<long long > q(k + 1,0);
    vector<long long > p(k + 1,0);
    for (int i = 0; i < k; ++i) {
        cin >> a[i];
    }
    for (int i = 0; i < k; ++i) {
        cin >> c[i];
    }
    q[0] = 1;
    for (int i = 1; i <= k; ++i) {
        q[i] = -c[i - 1];
    }
    int counter = k + 1;

    for (int i = 0; i < k; ++i) {
        long long sum = 0;
        for (int j = 0; j < i; ++j) {
            sum = (sum + c[j] * a[i - j - 1]) % maxMod;
        }
        p[i] = (a[i] - sum) % maxMod;
    }
    for (int i = k; i >= 0; --i) {
        if (p[i] != 0) break;
        counter--;
    }

    cout << counter - 1 << endl;
    for (int i = 0; i < counter; ++i) {
        cout << p[i] << " ";
    }
    cout << endl << q.size() - 1 << endl;
    for (long long i : q) {
        cout << i << " ";
    }



}