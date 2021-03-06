/*
 * MIT License
 * Copyright (c) 2017-2018 nuls.io
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.nuls.block.storage;

import io.nuls.base.data.BlockHeader;
import io.nuls.base.data.NulsDigestData;
import io.nuls.block.manager.ContextManager;
import io.nuls.block.model.po.BlockHeaderPo;
import io.nuls.block.service.BlockStorageService;
import io.nuls.db.service.RocksDBService;
import io.nuls.tools.core.inteceptor.ModularServiceMethodInterceptor;
import io.nuls.tools.core.ioc.SpringLiteContext;
import io.nuls.tools.log.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.nuls.block.constant.Constant.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BlockStorageServiceImplTest {

    private BlockHeaderPo header;
    private BlockStorageService service;

    @Before
    public void setUp() throws Exception {
        SpringLiteContext.init("io.nuls.block", new ModularServiceMethodInterceptor());
        service = ContextManager.getServiceBean(BlockStorageService.class);
        header = new BlockHeaderPo();
        header.setHeight(1212);
        header.setHash(NulsDigestData.calcDigestData("jyc".getBytes()));
        service.init(CHAIN_ID);
    }

    @After
    public void tearDown() throws Exception {
        service.destroy(CHAIN_ID);
        header = null;
        service = null;
    }

    @Test
    public void save() throws Exception {
        service.save(CHAIN_ID, header);
        assertNotNull(service.query(CHAIN_ID, header.getHeight()));
    }

    @Test
    public void remove() throws Exception {
        service.save(CHAIN_ID, header);
        service.remove(CHAIN_ID, header.getHeight());
        assertNull(service.query(CHAIN_ID, header.getHeight()));
    }

}