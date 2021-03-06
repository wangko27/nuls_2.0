package io.nuls.poc.utils.thread.process;

import io.nuls.base.data.Block;
import io.nuls.base.data.BlockExtendsData;
import io.nuls.base.data.BlockHeader;
import io.nuls.base.data.Transaction;
import io.nuls.poc.constant.ConsensusConstant;
import io.nuls.poc.constant.ConsensusErrorCode;
import io.nuls.poc.model.bo.BlockData;
import io.nuls.poc.model.bo.Chain;
import io.nuls.poc.model.bo.consensus.ConsensusStatus;
import io.nuls.poc.model.bo.round.MeetingMember;
import io.nuls.poc.model.bo.round.MeetingRound;
import io.nuls.poc.utils.manager.ConsensusManager;
import io.nuls.poc.utils.manager.RoundManager;
import io.nuls.tools.core.ioc.SpringLiteContext;
import io.nuls.tools.data.DateUtils;
import io.nuls.tools.exception.NulsException;
import io.nuls.tools.log.Log;
import io.nuls.tools.thread.TimeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * 共识处理器
 * Consensus processor
 *
 * @author tag
 * 2018/11/15
 * */
public class ConsensusProcess {
    private RoundManager roundManager = SpringLiteContext.getBean(RoundManager.class);


    private boolean hasPacking;

    public void process(Chain chain){
        try {
            boolean canPackage = checkCanPackage(chain);
            if (!canPackage) {
                return;
            }
            doWork(chain);
        }catch (NulsException e){
            Log.error(e.getMessage());
        }
    }

    /**
     * 检查节点打包状态
     * Check node packing status
     * */
    private boolean checkCanPackage(Chain chain) throws NulsException{
        if(chain == null ){
            throw new NulsException(ConsensusErrorCode.CHAIN_NOT_EXIST);
        }
        /*
        检查模块状态是否为运行中
        Check whether the module status is in operation
        */
        if (chain.getConsensusStatus().ordinal() <= ConsensusStatus.WAIT_RUNNING.ordinal()) {
            return false;
        }

        /*todo
        检查网络状态是否正常（调用网络模块接口获取当前链接节点数）
        Check whether the network status is normal
         */
        int availableNodes = 5;

        if(availableNodes < ConsensusConstant.ALIVE_MIN_NODE_COUNT){
            return false;
        }

        /*
        检查节点状态是否可打包(区块管理模块同步完成之后设置该状态)
        Check whether the node status can be packaged (set up after the block management module completes synchronization)
        */
        if(!chain.isCanPacking()){
            return false;
        }
        return true;
    }


    private void doWork(Chain chain)throws NulsException{
        /*
        检查节点状态
        Check node status
        */
        if (chain.getConsensusStatus().ordinal() < ConsensusStatus.RUNNING.ordinal()) {
            return;
        }

        /*
        获取当前轮次信息并验证轮次信息
        Get current round information
         */
        MeetingRound round = roundManager.getOrResetCurrentRound(chain,true);
        if (round == null) {
            return;
        }
        MeetingMember member = round.getMyMember();
        if(member == null){
            return;
        }

        /*
        如果是共识节点则判断是否轮到自己出块
        1.节点是否正在打包
        2.当前时间是否处于节点打包开始时间和结束时间之间
        If it's a consensus node, it's time to decide whether it's your turn to come out of the block.
        1. Is the node packing?
        2. Is the current time between the start and end of the node packing?
        */
        if(!hasPacking && member.getPackStartTime() < TimeService.currentTimeMillis() && member.getPackEndTime() > TimeService.currentTimeMillis()){
            hasPacking = true;
            try {
                if (Log.isDebugEnabled()) {
                    Log.debug("当前网络时间： " + DateUtils.convertDate(new Date(TimeService.currentTimeMillis())) + " , 我的打包开始时间: " +
                            DateUtils.convertDate(new Date(member.getPackStartTime())) + " , 我的打包结束时间: " +
                            DateUtils.convertDate(new Date(member.getPackEndTime())) + " , 当前轮开始时间: " +
                            DateUtils.convertDate(new Date(round.getStartTime())) + " , 当前轮结束开始时间: " +
                            DateUtils.convertDate(new Date(round.getEndTime())));
                }
                packing(chain, member, round);
            } catch (Exception e) {
                Log.error(e);
            }
            while (member.getPackEndTime() > TimeService.currentTimeMillis()) {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    Log.error(e);
                }
            }
            hasPacking = false;
        }
    }

    private void packing(Chain chain,MeetingMember self, MeetingRound round) throws IOException, NulsException {
        /*
        等待出块
        Wait for blocks
        */
        waitReceiveNewestBlock(chain,self, round);
        long start = System.currentTimeMillis();
        Block block = doPacking(chain, self, round);
        Log.info("doPacking use:" + (System.currentTimeMillis() - start) + "ms");

        /*
        * 打包完成之后，查看打包区块和主链最新区块是否连续，如果不连续表示打包过程中收到了上一个共识节点打包的区块，此时本地节点需要重新打包区块
        * After packaging, check whether the packaged block and the latest block in the main chain are continuous. If the block is not continuous,
        * the local node needs to repackage the block when it receives the packaged block from the previous consensus node in the packaging process.
        */
        BlockHeader header =chain.getNewestHeader();
        boolean rePacking = !block.getHeader().getPreHash().equals(header.getHash());
        if(rePacking){
            start = System.currentTimeMillis();
            block=doPacking(chain, self, round);
            Log.info("doPacking use:" + (System.currentTimeMillis() - start) + "ms");
        }
        if (null == block) {
            Log.error("make a null block");
            return;
        }
        //todo 打包成功后将区块传给区块管理模块广播
    }

    /**
     * 是否到达节点出块的时间点，如果本地最新区块为本轮次中上一节点所出，则直接打包出块，否则等待一定时间之后如果还没有接收到上一节点出的块则直接打包出块
     * Whether or not to arrive at the time point when the node is out of the block, if the latest local block is out of the previous node in this round, it will be packaged directly.
     * Otherwise, if the block from the previous node has not been received after waiting for a certain time, it will be packed directly.
     * */
    private boolean waitReceiveNewestBlock(Chain chain,MeetingMember self, MeetingRound round){
        long timeout = chain.getConfig().getPackingInterval()/2;
        long endTime = self.getPackStartTime() + timeout;
        boolean hasReceiveNewestBlock = false;
        try {
            while (!hasReceiveNewestBlock) {
                /*
                判断本地最新区块是否为轮次中上一个节点所出
                Determine whether the latest local block is from the last node in the round
                */
                hasReceiveNewestBlock = hasReceiveNewestBlock(chain, self, round);
                if (hasReceiveNewestBlock) {
                    break;
                }
                Thread.sleep(100L);
                if (TimeService.currentTimeMillis() >= endTime) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            Log.error(e);
        }
        return !hasReceiveNewestBlock;
    }

    /**
     * 判断本地最新区块是否为本轮次上一个区块所出
     * Judging whether the latest block in this region is from the last block in this round
     * */
    private boolean hasReceiveNewestBlock(Chain chain,MeetingMember self, MeetingRound round){
        int myIndex = self.getPackingIndexOfRound();
        MeetingMember preMember;
        MeetingRound  preRound = round;
        /*
        如果当前节点为该轮次第一个出块节点，则本地最新区块应该是上一轮的最后一个出块节点所出
        If the current node is the first out-of-block node in the round, the latest local block should be the last out-of-block node in the previous round.
        */
        if(myIndex == 1){
            preRound = round.getPreRound();
            if(preRound == null){
                Log.error("PreRound is null!");
                return true;
            }
            preMember = preRound.getMember(preRound.getMemberCount());
        }else{
            preMember = round.getMember(self.getPackingIndexOfRound()-1);
        }
        if (preMember == null) {
            return true;
        }

        /*
        比较本地最新区块出块地址与上一节点的出块地址是否相等，如果相等则表示上一节点已出块，当前节点可以出块了
        Comparing whether the block address of the latest local block is equal to that of the previous node, if equal,
        it means that the previous node has already blocked, and the current node can blocked.
        */
        BlockHeader bestBlockHeader = chain.getNewestHeader();
        BlockExtendsData blockRoundData = new BlockExtendsData(bestBlockHeader.getExtend());
        byte[] bestPackingAddress = bestBlockHeader.getPackingAddress();
        long bestRoundIndex = blockRoundData.getRoundIndex();
        int bestPackingIndex = blockRoundData.getPackingIndexOfRound();

        byte[] prePackingAddress = preMember.getAgent().getPackingAddress();
        long preRoundIndex = preRound.getIndex();
        int prePackingIndex = preMember.getPackingIndexOfRound();
        if (Arrays.equals(bestPackingAddress, prePackingAddress) && bestRoundIndex == preRoundIndex && bestPackingIndex == prePackingIndex) {
            return true;
        } else {
            return false;
        }
    }

    private Block doPacking(Chain chain, MeetingMember self, MeetingRound round) throws NulsException, IOException{
        BlockHeader bestBlock = chain.getNewestHeader();
        BlockData bd = new BlockData();
        bd.setHeight(bestBlock.getHeight() + 1);
        bd.setPreHash(bestBlock.getHash());
        bd.setTime(self.getPackEndTime());
        BlockExtendsData extendsData = new BlockExtendsData();
        extendsData.setRoundIndex(round.getIndex());
        extendsData.setConsensusMemberCount(round.getMemberCount());
        extendsData.setPackingIndexOfRound(self.getPackingIndexOfRound());
        extendsData.setRoundStartTime(round.getStartTime());
        bd.setExtendsData(extendsData);

        StringBuilder str = new StringBuilder();
        str.append(self.getAgent().getPackingAddress());
        str.append(" ,order:" + self.getPackingIndexOfRound());
        str.append(",packTime:" + new Date(self.getPackEndTime()));
        str.append("\n");
        Log.debug("pack round:" + str);
        //todo 从交易管理模块获取打包交易
        List<Transaction> packingTxList = new ArrayList<>();

        /*
        组装系统交易（CoinBase/红牌/黄牌）+ 创建区块
        Assembly System Transactions (CoinBase/Red/Yellow)+ Create blocks
        */
        ConsensusManager consensusManager = SpringLiteContext.getBean(ConsensusManager.class);
        consensusManager.addConsensusTx(chain,bestBlock,packingTxList,self,round);
        bd.setTxList(packingTxList);
        Block newBlock = consensusManager.createBlock(bd, self.getAgent().getPackingAddress());
        Log.info("make block height:" + newBlock.getHeader().getHeight() + ",txCount: " + newBlock.getTxs().size() + " , block size: " + newBlock.size() + " , time:" + DateUtils.convertDate(new Date(newBlock.getHeader().getTime())) + ",packEndTime:" +
                DateUtils.convertDate(new Date(self.getPackEndTime())));
        return newBlock;
    }
}
