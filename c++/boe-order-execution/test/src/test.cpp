#include "requests.h"

#include <gtest/gtest.h>

#include <cstring>
#include <ostream>
#include <type_traits>

namespace {

template <std::size_t Size>
class String
{
    char data_[Size + 1];
public:
    String(const unsigned char * src)
    {
        std::memcpy(data_, src, Size);
        data_[Size] = '\0';
    }
    String(const String & other)
    {
        std::memcpy(data_, other.data_, Size + 1);
    }

    friend bool operator == (const std::string & lhs, const String & rhs)
    { return lhs == rhs.data_; }
    friend bool operator != (const std::string & lhs, const String & rhs)
    { return !(lhs == rhs); }
    friend bool operator == (const String & lhs, const std::string & rhs)
    { return rhs == lhs; }
    friend bool operator != (const String & lhs, const std::string & rhs)
    { return rhs != lhs; }
    friend std::ostream & operator << (std::ostream & strm, const String & s)
    { return strm << s.data_; }
};

template <class T>
inline constexpr bool is_string_v = false;
template <std::size_t Size>
inline constexpr bool is_string_v<String<Size>> = true;

template <class T>
inline constexpr std::size_t string_size_v = 0;
template <std::size_t Size>
inline constexpr std::size_t string_size_v<String<Size>> = Size;

struct integral_type {};
struct string_type {};

template <class T>
struct any_to_false : std::false_type {};

template <class T>
T read_value_impl(const unsigned char * data, const std::size_t offset, const integral_type)
{
    const T * x = reinterpret_cast<const T *>(data + offset);
    return *x;
}

template <std::size_t Size>
String<Size> read_value_impl(const unsigned char * data, const std::size_t offset, const string_type)
{
    return {data + offset};
}

template <class T>
T read_value_impl(const unsigned char * data, const std::size_t offset)
{
    if constexpr (std::is_integral_v<T>) {
        return read_value_impl<T>(data, offset, integral_type{});
    }
    else if constexpr (is_string_v<T>) {
        return read_value_impl<string_size_v<T>>(data, offset, string_type{});
    }
    else {
        static_assert(any_to_false<T>::value, "Non-implemented type");
        return {};
    }
}

template <>
double read_value_impl<double>(const unsigned char * data, const std::size_t offset)
{
    const int64_t * x = reinterpret_cast<const int64_t *>(data + offset);
    return static_cast<double>(*x) / 10000;
}

template <class T, class A>
T read_value(const A & data, const std::size_t offset)
{
    return read_value_impl<T>(data.data(), offset);
}

struct NewOrderData
{
    static char convert_side(const Side side)
    {
        switch (side) {
            case Side::Buy: return '1';
            case Side::Sell: return '2';
        }
        return 0;
    }
    static char convert_ord_type(const OrdType ord_type)
    {
        switch (ord_type) {
            case OrdType::Market: return '1';
            case OrdType::Limit: return '2';
            case OrdType::Pegged: return 'P';
        }
        return 0;
    }
    static char convert_time_in_force(const TimeInForce time_in_force)
    {
        switch (time_in_force) {
            case TimeInForce::Day: return '0';
            case TimeInForce::IOC: return '3';
            case TimeInForce::GTD: return '6';
        }
        return 0;
    }
    static char convert_capacity(const Capacity capacity)
    {
        switch (capacity) {
            case Capacity::Agency: return 'A';
            case Capacity::Principal: return 'P';
            case Capacity::RisklessPrincipal: return 'R';
        }
        return 0;
    }

    const unsigned seq_no;
    const std::string cl_ord_id;
    const char side;
    const double volume;
    const double price;
    const char ord_type;
    const char time_in_force;
    const double max_floor;
    const std::string symbol;
    const char capacity;
    const std::string account;
    const std::array<unsigned char, calculate_size(RequestType::New)> data;

    NewOrderData(
              const unsigned seq_no_
            , std::string cl_ord_id_
            , const Side side_
            , const double volume_
            , const double price_
            , const OrdType ord_type_
            , const TimeInForce time_in_force_
            , const double max_floor_
            , std::string symbol_
            , const Capacity capacity_
            , std::string account_
            )
        : seq_no(seq_no_)
        , cl_ord_id(std::move(cl_ord_id_))
        , side(convert_side(side_))
        , volume(volume_)
        , price(price_)
        , ord_type(convert_ord_type(ord_type_))
        , time_in_force(convert_time_in_force(time_in_force_))
        , max_floor(max_floor_)
        , symbol(std::move(symbol_))
        , capacity(convert_capacity(capacity_))
        , account(std::move(account_))
        , data(create_new_order_request(seq_no, cl_ord_id, side_, volume, price, ord_type_, time_in_force_, max_floor, symbol, capacity_, account))
    {}
};

class TestRequest
{
    unsigned seq_no_ = 1;
    std::string cl_ord_id_{"ORD101"};
    Side side_ = Side::Buy;
    double volume_ = 100;
    double price_ = 12.505;
    OrdType ord_type_ = OrdType::Limit;
    TimeInForce time_in_force_ = TimeInForce::Day;
    double max_floor_ = 10;
    std::string symbol_{"AAPl"};
    Capacity capacity_ = Capacity::Principal;
    std::string account_{"ACC331"};
public:
#define FIELD(field, type) \
    TestRequest && field(type field ## _v) && \
    { \
        field ## _ = std::move(field ## _v); \
        return std::move(*this); \
    }
    FIELD(seq_no, unsigned)
    FIELD(cl_ord_id, std::string)
    FIELD(side, Side)
    FIELD(volume, double)
    FIELD(price, double)
    FIELD(ord_type, OrdType)
    FIELD(time_in_force, TimeInForce)
    FIELD(max_floor, double)
    FIELD(symbol, std::string)
    FIELD(capacity, Capacity)
    FIELD(account, std::string)
#undef FIELD
    operator NewOrderData () &&
    {
        return {
            seq_no_
          , std::move(cl_ord_id_)
          , side_
          , volume_
          , price_
          , ord_type_
          , time_in_force_
          , max_floor_
          , std::move(symbol_)
          , capacity_
          , std::move(account_)
        };
    }
};

} // anonymous namespace

TEST(NewOrderTest, simple_size_check)
{
    const NewOrderData nod = TestRequest();
    EXPECT_EQ(78, nod.data.size());
}

TEST(NewOrderTest, read_values)
{
    const NewOrderData nod = TestRequest()
        .volume(33)
        .time_in_force(TimeInForce::IOC)
        .capacity(Capacity::Agency)
        .account("HOUSE1");
    const auto header = read_value<uint16_t>(nod.data, 0);
    const auto msg_len = read_value<uint16_t>(nod.data, 2);
    const auto msg_type = read_value<uint8_t>(nod.data, 4);
    const auto seq_no = read_value<uint32_t>(nod.data, 6);
    const auto cl_ord_id = read_value<String<20>>(nod.data, 10);
    const auto side = read_value<char>(nod.data, 30);
    const auto volume = read_value<uint32_t>(nod.data, 31);
    const auto price = read_value<double>(nod.data, 39);
    const auto ord_type = read_value<char>(nod.data, 47);
    const auto time_in_force = read_value<char>(nod.data, 48);
    const auto max_floor = read_value<uint32_t>(nod.data, 49);
    const auto symbol = read_value<String<8>>(nod.data, 53);
    const auto capacity = read_value<char>(nod.data, 61);
    const auto account = read_value<String<16>>(nod.data, 62);
    EXPECT_EQ(0xBABA, header);
    EXPECT_EQ(nod.data.size()-2, msg_len);
    EXPECT_EQ(0x38, msg_type);
    EXPECT_EQ(nod.seq_no, seq_no);
    EXPECT_EQ(nod.cl_ord_id, cl_ord_id);
    EXPECT_EQ(nod.side, side);
    EXPECT_EQ(nod.volume, volume);
    EXPECT_EQ(nod.price, price);
    EXPECT_EQ(nod.ord_type, ord_type);
    EXPECT_EQ(nod.time_in_force, time_in_force);
    EXPECT_EQ(nod.max_floor, max_floor);
    EXPECT_EQ(nod.symbol, symbol);
    EXPECT_EQ(nod.capacity, capacity);
    EXPECT_EQ(nod.account, account);
}

constexpr auto eps = 1.e-9;

TEST(OrderExecutionTest, decode)
{
    const std::vector<unsigned char> message = {
        0xBA, 0xBA,
        0x5A, 0x00,
        0x2C,
        0x03,
        0x64, 0x00, 0x00, 0x00,
        0xE0, 0xFA, 0x20, 0xF7, 0x36, 0x71, 0xF8, 0x11,
        0x41, 0x42, 0x43, 0x31, 0x32, 0x33, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x01, 0xF0, 0xB7, 0xD9, 0x71, 0x21, 0x00, 0x00,
        0x64, 0x00, 0x00, 0x00,
        0x08, 0xE2, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x41,
        0x00,
        0x42, 0x41, 0x54, 0x53,
        0x00,
        0x08,
        0x00,
        0x01,
        0x00,
        0x00,
        0x00,
        0x00,
        0x80,
        0x01,
        0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x32,
        0x58, 0x53, 0x54, 0x4F,
        0x52, 0x47
    };
    ExecutionDetails decodedData = decode_order_execution(message);
    EXPECT_EQ(decodedData.cl_ord_id, "ABC123");
    EXPECT_EQ(decodedData.exec_id, "D19800001");
    EXPECT_NEAR(decodedData.filled_volume, 100, eps);
    EXPECT_NEAR(decodedData.active_volume, 0., eps);
    EXPECT_NEAR(decodedData.price, 12.34, eps);
    EXPECT_EQ(decodedData.liquidity_indicator, LiquidityIndicator::Added);
    EXPECT_EQ(decodedData.symbol, "ABCDEFG2");
    EXPECT_EQ(decodedData.last_mkt, "XSTO");
    EXPECT_EQ(decodedData.fee_code, "RG");
}

TEST(OrderExecutionTest, bitfields)
{
    auto availableBitfields = request_optional_fields_for_message(ResponseType::OrderExecution);
    ASSERT_EQ(8, availableBitfields.size());
    bool referenceBitfields[15][8] = {
        { false, false, false, false, false, false, false, false },
        { true, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, true },
        { true, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
        { false, false, false, false, false, false, false, false },
    };
    for (std::size_t i = 0; i < availableBitfields.size(); ++i) {
        for (unsigned j = 0; j < 8; ++j) {
            EXPECT_EQ(referenceBitfields[i][j], static_cast<bool>(availableBitfields[i] & (1u << j)));
        }
    }
}
