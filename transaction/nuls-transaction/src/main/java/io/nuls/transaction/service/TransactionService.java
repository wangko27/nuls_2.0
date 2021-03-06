package io.nuls.transaction.service;

import io.nuls.base.data.NulsDigestData;
import io.nuls.base.data.Transaction;
import io.nuls.tools.basic.Result;
import io.nuls.transaction.model.bo.TxRegister;
import io.nuls.transaction.model.dto.BlockHeaderDigestDTO;
import io.nuls.transaction.model.dto.CoinDTO;

import java.util.List;

/**
 * @author: Charlie
 * @date: 2018/11/22
 */
public interface TransactionService {

    /**
     * 注册交易
     * Register transaction
     *
     * @param txRegister
     * @return Result
     */
    Result register(TxRegister txRegister);

    /**
     * 收到一个新的交易
     * Received a new transaction
     *
     * @param transaction
     * @return Result
     */
    Result newTx(int chainId, Transaction transaction);

    /**
     * 获取一笔交易
     * get a transaction
     *
     * @param hash
     * @return Result
     */
    Result getTransaction(NulsDigestData hash);


    /**
     * 创建不包含多签地址跨链交易，支持多普通地址
     * Create a cross-chain transaction
     *
     * @param currentChainId 当前链的id Current chainId
     * @param listFrom 交易的转出者数据 payer coins
     * @param listTo 交易的接收者数据 payee  coins
     * @param remark 交易备注 remark
     * @return Result
     */
    Result createCrossTransaction(int currentChainId, List<CoinDTO> listFrom, List<CoinDTO> listTo, String remark);

    /**
     * 创建跨链多签签名地址交易
     * 所有froms中有且仅有一个地址，并且只能是多签地址，但可以包含多个资产(from)
     *
     * @param currentChainId 当前链的id Current chainId
     * @param listFrom 交易的转出者数据 payer coins
     * @param listTo 交易的接收者数据 payee  coins
     * @param remark 交易备注 remark
     * @return Result
     */
    Result createCrossMultiTransaction(int currentChainId, List<CoinDTO> listFrom, List<CoinDTO> listTo, String remark);


    Result crossTransactionValidator(int chainId, Transaction transaction);
    Result crossTransactionCommit(int chainId, Transaction transaction, BlockHeaderDigestDTO blockHeader);
    Result crossTransactionRollback(int chainId, Transaction transaction, BlockHeaderDigestDTO blockHeader);

}
