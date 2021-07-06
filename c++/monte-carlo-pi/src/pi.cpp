#include "pi.h"

#include "random_gen.h"

double calculate_pi(unsigned long runs) {
    double goodVariants = 0;

    for (unsigned long i = 0; i < runs; ++i) {
        double a = get_random_number();
        double b = get_random_number();
        if ((a * a + b * b) <= 1) {
            goodVariants++;
        }
    }
    if (runs == 0) {
        return 0;
    }
    return (goodVariants / runs) * 4;
}
