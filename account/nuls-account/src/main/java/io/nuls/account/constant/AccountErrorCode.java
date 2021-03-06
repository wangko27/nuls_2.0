/*
 * MIT License
 *
 * Copyright (c) 2017-2018 nuls.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.nuls.account.constant;

import io.nuls.tools.constant.ErrorCode;

/**
 * @author: qinyifeng
 */
public interface AccountErrorCode {

    ErrorCode SUCCESS = ErrorCode.init("10000");
    ErrorCode FAILED = ErrorCode.init("10001");
    ErrorCode SYS_UNKOWN_EXCEPTION = ErrorCode.init("10002");
    ErrorCode DATA_PARSE_ERROR = ErrorCode.init("10003");
    ErrorCode THREAD_REPETITION = ErrorCode.init("10004");
    ErrorCode LANGUAGE_CANNOT_SET_NULL = ErrorCode.init("10005");
    ErrorCode IO_ERROR = ErrorCode.init("10006");
    ErrorCode DATA_SIZE_ERROR = ErrorCode.init("10007");
    ErrorCode CONFIG_ERROR = ErrorCode.init("10008");
    ErrorCode SIGNATURE_ERROR = ErrorCode.init("10009");
    ErrorCode REQUEST_DENIED = ErrorCode.init("10010");
    ErrorCode DATA_SIZE_ERROR_EXTEND = ErrorCode.init("10011");
    ErrorCode PARAMETER_ERROR = ErrorCode.init("10012");
    ErrorCode NULL_PARAMETER = ErrorCode.init("10013");
    ErrorCode DATA_ERROR = ErrorCode.init("10014");
    ErrorCode DATA_NOT_FOUND = ErrorCode.init("10015");
    ErrorCode DOWNLOAD_VERSION_FAILD = ErrorCode.init("10016");
    ErrorCode PARSE_JSON_FAILD = ErrorCode.init("10017");
    ErrorCode FILE_OPERATION_FAILD = ErrorCode.init("10018");
    ErrorCode ILLEGAL_ACCESS_EXCEPTION = ErrorCode.init("10019");
    ErrorCode INSTANTIATION_EXCEPTION = ErrorCode.init("10020");
    ErrorCode UPGRADING = ErrorCode.init("10021");
    ErrorCode NOT_UPGRADING = ErrorCode.init("10022");
    ErrorCode VERSION_NOT_NEWEST = ErrorCode.init("10023");
    ErrorCode SERIALIZE_ERROR = ErrorCode.init("10024");
    ErrorCode DESERIALIZE_ERROR = ErrorCode.init("10025");
    ErrorCode HASH_ERROR = ErrorCode.init("10026");
    ErrorCode INSUFFICIENT_BALANCE = ErrorCode.init("10027");
    ErrorCode ADDRESS_IS_BLOCK_HOLE = ErrorCode.init("10028");
    ErrorCode ADDRESS_IS_NOT_BELONGS_TO_CHAIN = ErrorCode.init("10029");
    ErrorCode VALIDATORS_NOT_FULLY_EXECUTED = ErrorCode.init("10030");
    ErrorCode BLOCK_IS_NULL = ErrorCode.init("10031");
    ErrorCode VERSION_TOO_LOW = ErrorCode.init("10032");
    ErrorCode PUBKEY_REPEAT = ErrorCode.init("10033");
    ErrorCode COIN_OWNER_ERROR = ErrorCode.init("10034");
    ErrorCode NONEWVER = ErrorCode.init("10035");

    ErrorCode DB_TABLE_EXIST = ErrorCode.init("20009");
    ErrorCode DB_TABLE_NOT_EXIST = ErrorCode.init("20010");
    ErrorCode DB_TABLE_CREATE_ERROR = ErrorCode.init("20011");
    ErrorCode DB_SAVE_BATCH_ERROR = ErrorCode.init("20012");
    ErrorCode DB_SAVE_ERROR = ErrorCode.init("20013");
    ErrorCode DB_UPDATE_ERROR = ErrorCode.init("20014");
    ErrorCode DB_QUERY_ERROR = ErrorCode.init("20015");
    ErrorCode DB_DELETE_ERROR = ErrorCode.init("20016");

    ErrorCode PASSWORD_IS_WRONG = ErrorCode.init("30000");
    ErrorCode ACCOUNT_NOT_EXIST = ErrorCode.init("30001");
    ErrorCode ACCOUNT_IS_ALREADY_ENCRYPTED = ErrorCode.init("30002");
    ErrorCode ACCOUNT_EXIST = ErrorCode.init("30003");
    ErrorCode ADDRESS_ERROR = ErrorCode.init("30004");
    ErrorCode ALIAS_EXIST = ErrorCode.init("30005");
    ErrorCode ALIAS_NOT_EXIST = ErrorCode.init("30006");
    ErrorCode ACCOUNT_ALREADY_SET_ALIAS = ErrorCode.init("30007");
    ErrorCode ACCOUNT_UNENCRYPTED = ErrorCode.init("30008");
    ErrorCode ALIAS_CONFLICT = ErrorCode.init("30009");
    ErrorCode HAVE_ENCRYPTED_ACCOUNT = ErrorCode.init("30010");
    ErrorCode HAVE_UNENCRYPTED_ACCOUNT = ErrorCode.init("30011");
    ErrorCode PRIVATE_KEY_WRONG = ErrorCode.init("30012");
    ErrorCode ALIAS_ROLLBACK_ERROR = ErrorCode.init("30013");
    ErrorCode ACCOUNTKEYSTORE_FILE_NOT_EXIST = ErrorCode.init("30014");
    ErrorCode ACCOUNTKEYSTORE_FILE_DAMAGED = ErrorCode.init("30015");
    ErrorCode ALIAS_FORMAT_WRONG = ErrorCode.init("30016");
    ErrorCode PASSWORD_FORMAT_WRONG = ErrorCode.init("30017");
    ErrorCode DECRYPT_ACCOUNT_ERROR = ErrorCode.init("30018");
    ErrorCode ACCOUNT_IS_ALREADY_ENCRYPTED_AND_LOCKED = ErrorCode.init("30019");
    ErrorCode REMARK_TOO_LONG = ErrorCode.init("30020");
    ErrorCode INPUT_TOO_SMALL = ErrorCode.init("30021");
    ErrorCode MUST_BURN_A_NULS = ErrorCode.init("30022");
    ErrorCode SIGN_COUNT_TOO_LARGE = ErrorCode.init("30023");

    ErrorCode COINDATA_NOT_FOUND = ErrorCode.init("31016");
    ErrorCode TX_TYPE_ERROR = ErrorCode.init("31017");


}
