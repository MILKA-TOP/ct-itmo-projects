#include "requests.h"

namespace {

void encode_new_order_opt_fields(unsigned char * bitfield_start,
                                 const double price,
                                 const char ord_type,
                                 const char time_in_force,
                                 const unsigned max_floor,
                                 const std::string & symbol,
                                 const char capacity,
                                 const std::string & account)
{
    auto * p = bitfield_start + new_order_bitfield_num();
#define FIELD(name, bitfield_num, bit)                    \
    set_opt_field_bit(bitfield_start, bitfield_num, bit); \
    p = encode_field_##name(p, name);
#include "new_order_opt_fields.inl"
}

uint8_t encode_request_type(const RequestType type)
{
    switch (type) {
    case RequestType::New:
        return 0x38;
    }
    return 0;
}

unsigned char * add_request_header(unsigned char * start, unsigned length, const RequestType type, unsigned seq_no)
{
    *start++ = 0xBA;
    *start++ = 0xBA;
    start = encode(start, static_cast<uint16_t>(length));
    start = encode(start, encode_request_type(type));
    *start++ = 0;
    return encode(start, (seq_no));
}

char convert_side(const Side side)
{
    switch (side) {
    case Side::Buy: return '1';
    case Side::Sell: return '2';
    }
    return 0;
}

char convert_ord_type(const OrdType ord_type)
{
    switch (ord_type) {
    case OrdType::Market: return '1';
    case OrdType::Limit: return '2';
    case OrdType::Pegged: return 'P';
    }
    return 0;
}

char convert_time_in_force(const TimeInForce time_in_force)
{
    switch (time_in_force) {
    case TimeInForce::Day: return '0';
    case TimeInForce::IOC: return '3';
    case TimeInForce::GTD: return '6';
    }
    return 0;
}

char convert_capacity(const Capacity capacity)
{
    switch (capacity) {
    case Capacity::Agency: return 'A';
    case Capacity::Principal: return 'P';
    case Capacity::RisklessPrincipal: return 'R';
    }
    return 0;
}

} // anonymous namespace

std::array<unsigned char, calculate_size(RequestType::New)> create_new_order_request(const unsigned seq_no,
                                                                                     const std::string & cl_ord_id,
                                                                                     const Side side,
                                                                                     const double volume,
                                                                                     const double price,
                                                                                     const OrdType ord_type,
                                                                                     const TimeInForce time_in_force,
                                                                                     const double max_floor,
                                                                                     const std::string & symbol,
                                                                                     const Capacity capacity,
                                                                                     const std::string & account)
{
    static_assert(calculate_size(RequestType::New) == 78, "Wrong New Order message size");

    std::array<unsigned char, calculate_size(RequestType::New)> msg;
    auto * p = add_request_header(&msg[0], msg.size() - 2, RequestType::New, seq_no);
    p = encode_text(p, cl_ord_id, 20);
    p = encode_char(p, convert_side(side));
    p = encode_binary4(p, static_cast<uint32_t>(volume));
    p = encode(p, static_cast<uint8_t>(new_order_bitfield_num()));
    encode_new_order_opt_fields(p,
                                price,
                                convert_ord_type(ord_type),
                                convert_time_in_force(time_in_force),
                                max_floor,
                                symbol,
                                convert_capacity(capacity),
                                account);
    return msg;
}

static std::string chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

std::string base36_from_long_long(long long numb)
{
    std::string out_string;
    while (numb > 0) {
        out_string.push_back(chars.at(numb % 36));
        numb /= 36;
    }
    std::reverse(out_string.begin(), out_string.end());
    return out_string;
}

std::string decode_byte_line_to_string(const std::vector<unsigned char> & message, int start_index, int length_of_line)
{
    std::string out_string;
    for (int i = start_index; i < start_index + length_of_line; ++i) {
        if (message[i] != 0) {
            out_string.push_back(message[i]);
        }
    }
    return out_string;
}

unsigned long long decode_byte_line_to_long(const std::vector<unsigned char> & message, int start_index, int end_index)
{
    unsigned long long out_number = 0;
    unsigned long long shift_level = 1;
    for (int i = start_index; i < end_index; i++) {
        out_number += message[i] * shift_level;
        shift_level *= 256;
    }
    return out_number;
}

/* В данной программе присутствует довольно простая возможность добавления нового считываемы элемента из опциональных полей.
 * Для этого достаточно изменить значение в методе request_optional_fields_for_message и добавить в конструкцию switch
 * необходимое нам считывание. Также при необходимости нужно добавить размер элементов опционаьлного поля в массив size_of_bitfield_elements.
 * */

ExecutionDetails decode_order_execution(const std::vector<unsigned char> & message)
{
    ExecutionDetails exec_details;
    std::vector<unsigned char> mask_of_bitfield_execution{0x7F, 0x7D, 0xDF, 0x00, 0x00, 0x03, 0x80, 0x09};
    std::vector<unsigned char> mask_of_input = request_optional_fields_for_message(ResponseType::OrderExecution);
    int size_of_bitfield_elements[8][8] = {{1, 8, 8, 1, 1, 1, 4, 0},
                                           {8, 0, 3, 1, 16, 4, 1, 0},
                                           {16, 4, 4, 1, 4, 0, 4, 3},
                                           {0, 0, 0, 0, 0, 0, 0, 0},
                                           {0, 0, 0, 0, 0, 0, 0, 0},
                                           {8, 1, 0, 0, 0, 0, 0, 0},
                                           {0, 0, 0, 0, 0, 0, 0, 4},
                                           {2, 0, 0, 4, 0, 0, 0, 0}};
    std::string temp_string_line;

    //cl_ord_id decoding;
    exec_details.cl_ord_id.assign(decode_byte_line_to_string(message, 18, 20));

    //exec_id decoding;
    temp_string_line = base36_from_long_long(decode_byte_line_to_long(message, 38, 46));
    exec_details.exec_id.assign(temp_string_line);

    //liquidity_indicator decoding;
    if (message[62] == 'A') {
        exec_details.liquidity_indicator = LiquidityIndicator::Added;
    }
    else if (message[62] == 'R') {
        exec_details.liquidity_indicator = LiquidityIndicator::Removed;
    }

    //filled_volume decoding;
    exec_details.filled_volume = static_cast<double>(decode_byte_line_to_long(message, 46, 50));

    //price decoding;
    exec_details.price = static_cast<double>(decode_byte_line_to_long(message, 50, 58)) / 10000;

    //active_volume decoding;
    exec_details.active_volume = static_cast<double>(decode_byte_line_to_long(message, 58, 62));

    int count_of_bitfields = message[69];
    int size_before_input_bitfield = 0;
    int count_of_checked_input_bitfield = 0;
    for (int i = 70; i < 70 + count_of_bitfields; ++i) {
        for (int j = 0; j < 8; ++j) {
            if ((mask_of_input[i - 70] >> j) & 1) {
                if ((message[i] >> j) & 1) {
                    temp_string_line = decode_byte_line_to_string(message, 70 + count_of_bitfields + size_before_input_bitfield, size_of_bitfield_elements[i - 70][j]);
                    switch (count_of_checked_input_bitfield) {
                    case 0:
                        exec_details.symbol.assign(temp_string_line);
                        break;
                    case 1:
                        exec_details.last_mkt.assign(temp_string_line);
                        break;
                    case 2:
                        exec_details.fee_code.assign(temp_string_line);
                        break;
                    }
                }
                count_of_checked_input_bitfield++;
            }
            if ((mask_of_bitfield_execution[i - 70] >> j) & (message[i] >> j) & 1) {
                size_before_input_bitfield += size_of_bitfield_elements[i - 70][j];
            }
        }
    }

    return exec_details;
}

std::vector<unsigned char> request_optional_fields_for_message(const ResponseType type)
{
    if (type == ResponseType::OrderExecution) {
        return {0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x80, 0x01};
    }
    return {};
}
