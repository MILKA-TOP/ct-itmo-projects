#include <iostream>
#include <cmath>
#include <vector>
#include <set>
#include <queue>
#include <algorithm>
#include <numeric>

using namespace std;
long long maxMod = 998244353;
vector<long long> input_p;
int n, m;

long long gcd(long long a, long long updM, long long &f1, long long &f2) {
    if (a == 0) {
        f1 = 0;
        f2 = 1;
        return updM;
    }

    long long f11, f22;
    long long newVal = gcd(updM % a, a, f11, f22);
    f2 = f11;
    f1 = f22 - (updM / a) * f11;
    return newVal;
}

vector<long long> multiFuncij(vector<long long> multiVector1, vector<long long> multiVector2) {
    vector<long long> results(m + 1, 0);
    for (int i = 0; i < m + 1; ++i) {
        for (int j = 0; j <= i; ++j) {
            results[i] = (results[i] + (multiVector2[i - j] * multiVector1[j] + maxMod) % maxMod + maxMod) % maxMod;
            // cout << "NOW: " << (results[i + j] + (input_p[i] * multiVector[j] + maxMod) % maxMod + maxMod) % maxMod << " "
            //     << input_p[i] << " " << multiVector[j] << endl;
        }
    }
    return results;

}

void finalOperation(vector<long long> checkedExpLnSqrt) {
    vector<long long> now_vector(m + 1, 0);
    vector<long long> results(m + 1, 0);
    now_vector[0] = 1;
    for (int i = 0; i < m; ++i) {
        for (int j = 0; j <= m; ++j) {
            results[j] = (results[j] + (checkedExpLnSqrt[i] * now_vector[j] + maxMod) % maxMod + maxMod) % maxMod;
        }
        cout << results[i] << " ";
        now_vector = multiFuncij(now_vector, input_p);
    }
    cout << endl;

}


int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout.tie(NULL);
    cin >> n >> m;
    vector<long long> sqrt_v(m + 1, 0);
    vector<long long> exp_v(m + 1, 0);
    vector<long long> ln_v(m + 1, 0);
    vector<long long> res_ln(250, 0);
    vector<long long> res_exp(250, 0);
    vector<long long> res_sqrt(250, 0);
    input_p.resize(250, 0);
    exp_v[0] = res_exp[0] = 1;
    sqrt_v[0] = sqrt_v[0] = 1;
    ln_v[0] = res_ln[0] = 0;
    for (int i = 0; i < n + 1; ++i) {
        cin >> input_p[i];
    }
    long long prevFact = 1;
    long long prevEvenFact = 1;
    long long prevPow = 1;

    /*
     * sqrt: (−1)^(k−1) / (2^k * (k!)) * 1 * 3 * 5⋯(2k−3)
     * exp: 1 / (k!)
     * ln: (-1)^(k+1)/k
     * */
    for (int i = 1; i < m + 1; ++i) {
        long long x1, y1, x2, y2, x3, y3;
        long long modFact = (prevFact * i + maxMod) % maxMod;
        long long powFact = (prevPow * 2 + maxMod) % maxMod;
        int nowBool = ((i % 2 != 0) ? 1 : -1);
        long long evenFact = (i > 1) ? (prevEvenFact * (2 * i - 3) + maxMod) % maxMod : prevEvenFact;
        gcd(modFact, maxMod, x1, y1);
        gcd((nowBool * i + maxMod) % maxMod, maxMod, x2, y2);
        gcd(((nowBool * modFact + maxMod) % maxMod * powFact + maxMod) % maxMod, maxMod, x3, y3);
        exp_v[i] = (x1 % maxMod + maxMod) % maxMod;
        ln_v[i] = (x2 % maxMod + maxMod) % maxMod;
        sqrt_v[i] = (evenFact * ((x3 % maxMod + maxMod) % maxMod) + maxMod) % maxMod;

        prevFact = (prevFact * i + maxMod) % maxMod;
        prevPow = (prevPow * 2 + maxMod) % maxMod;
        prevEvenFact = evenFact;
    }
    /*for (int i = 0; i < m; ++i) {
        cout << sqrt_v[i] << " ";
    }
    cout << endl;
    for (int i = 0; i < m; ++i) {
        cout << exp_v[i] << " ";
    }
    cout << endl;
    for (int i = 0; i < m; ++i) {
        cout << ln_v[i] << " ";
    }

    cout << endl << endl;*/
    finalOperation(sqrt_v);
    finalOperation(exp_v);
    finalOperation(ln_v);


}