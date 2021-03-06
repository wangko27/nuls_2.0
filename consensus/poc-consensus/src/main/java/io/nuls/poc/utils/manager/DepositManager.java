package io.nuls.poc.utils.manager;

import io.nuls.base.data.NulsDigestData;
import io.nuls.poc.model.bo.Chain;
import io.nuls.poc.model.bo.tx.txdata.Deposit;
import io.nuls.poc.model.po.DepositPo;
import io.nuls.poc.storage.DepositStorageService;
import io.nuls.poc.utils.compare.DepositComparator;
import io.nuls.tools.core.annotation.Autowired;
import io.nuls.tools.core.annotation.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 委托信息管理类，负责委托信息相关处理
 * Delegated information management category, responsible for delegated information related processing
 *
 * @author tag
 * 2018/12/5
 * */
@Component
public class DepositManager {
    @Autowired
    private DepositStorageService depositStorageService;

    /**
     * 初始化委托信息
     * Initialize delegation information
     *
     * @param chain 链信息/chain info
     * */
    public void loadDeposits(Chain chain) throws Exception{
        List<Deposit> allDepositList = new ArrayList<>();
        List<DepositPo> poList = depositStorageService.getList(chain.getConfig().getChainId());
        for (DepositPo po : poList) {
            Deposit deposit = poToDeposit(po);
            allDepositList.add(deposit);
        }
        Collections.sort(allDepositList, new DepositComparator());
        chain.setDepositList(allDepositList);
    }

    /**
     * 添加委托缓存
     * Add delegation cache
     *
     * @param chain       chain info
     * @param deposit     deposit info
     * */
    public void addDeposit(Chain chain,Deposit deposit) {
        chain.getDepositList().add(deposit);
    }

    /**
     * 修改委托缓存
     * modify delegation cache
     *
     * @param chain     chain
     * @param deposit     deposit info
     * */
    public void updateDeposit(Chain chain,Deposit deposit){
        List<Deposit> depositList = chain.getDepositList();
        if(depositList == null || depositList.size() == 0){
            depositList.add(deposit);
            return;
        }
        for (int index = 0;index <= depositList.size() ; index++ ){
            if(deposit.getTxHash().equals(depositList.get(index))){
                depositList.set(index,deposit);
            }
        }
    }

    /**
     * 删除指定链的委托信息
     * Delete delegate information for a specified chain
     *
     * @param chain      chain nfo
     * @param txHash     创建该委托交易的Hash/Hash to create the delegated transaction
     * */
    public void removeDeposit(Chain chain, NulsDigestData txHash) throws Exception{
        List<Deposit> depositList = chain.getDepositList();
        if(depositList == null || depositList.size() == 0){
            return;
        }
        for (Deposit deposit:depositList) {
            if(Arrays.equals(txHash.serialize(),deposit.getTxHash().serialize())){
                depositList.remove(deposit);
                return;
            }
        }
    }

    /**
     * DepositPo to Deposit
     *
     * @param po DepositPo
     * @return Deposit
     * */
    public Deposit poToDeposit(DepositPo po) {
        Deposit deposit = new Deposit();
        deposit.setDeposit(po.getDeposit());
        deposit.setAgentHash(po.getAgentHash());
        deposit.setTime(po.getTime());
        deposit.setDelHeight(po.getDelHeight());
        deposit.setBlockHeight(po.getBlockHeight());
        deposit.setAddress(po.getAddress());
        deposit.setTxHash(po.getTxHash());
        return deposit;
    }

    /**
     * DepositPo to Deposit
     *
     * @param deposit Deposit
     * @return DepositPo
     * */
    public DepositPo depositToPo(Deposit deposit) {
        DepositPo po = new DepositPo();
        po.setTxHash(deposit.getTxHash());
        po.setAddress(deposit.getAddress());
        po.setAgentHash(deposit.getAgentHash());
        po.setBlockHeight(deposit.getBlockHeight());
        po.setDelHeight(deposit.getDelHeight());
        po.setDeposit(deposit.getDeposit());
        po.setTime(deposit.getTime());
        return po;
    }
}
