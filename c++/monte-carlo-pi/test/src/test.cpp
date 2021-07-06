#include "pi.h"

#include <gtest/gtest.h>

#include <cmath>

TEST(MonteCarloTest, zero_iterations)
{
    EXPECT_FALSE(std::isnan(calculate_pi(0)));
}

TEST(MonteCarloTest, pi_test)
{
    EXPECT_NEAR(calculate_pi(1000000), 3.14, 1.e-2);
    EXPECT_NEAR(calculate_pi(100000000), 3.141, 1.e-3);
}
