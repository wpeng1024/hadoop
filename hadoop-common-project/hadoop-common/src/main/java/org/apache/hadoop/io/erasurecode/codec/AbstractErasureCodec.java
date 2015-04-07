/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.io.erasurecode.codec;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.erasurecode.ECSchema;
import org.apache.hadoop.io.erasurecode.coder.*;
import org.apache.hadoop.io.erasurecode.grouper.BlockGrouper;

/**
 * Abstract Erasure Codec that implements {@link ErasureCodec}.
 */
public abstract class AbstractErasureCodec extends Configured
    implements ErasureCodec {

  private ECSchema schema;

  @Override
  public void setSchema(ECSchema schema) {
    this.schema = schema;
  }

  public String getName() {
    return schema.getCodecName();
  }

  protected ECSchema getSchema() {
    return schema;
  }

  @Override
  public BlockGrouper createBlockGrouper() {
    BlockGrouper blockGrouper = new BlockGrouper();
    blockGrouper.setSchema(getSchema());

    return blockGrouper;
  }

  @Override
  public ErasureCoder createEncoder() {
    ErasureCoder encoder = doCreateEncoder();
    prepareErasureCoder(encoder);
    return encoder;
  }

  /**
   * Create a new encoder instance to be initialized afterwards.
   * @return encoder
   */
  protected abstract ErasureCoder doCreateEncoder();

  @Override
  public ErasureCoder createDecoder() {
    ErasureCoder decoder = doCreateDecoder();
    prepareErasureCoder(decoder);
    return decoder;
  }

  /**
   * Create a new decoder instance to be initialized afterwards.
   * @return decoder
   */
  protected abstract ErasureCoder doCreateDecoder();

  private void prepareErasureCoder(ErasureCoder erasureCoder) {
    if (getSchema() == null) {
      throw new RuntimeException("No schema been set yet");
    }

    erasureCoder.setConf(getConf());
    erasureCoder.initialize(getSchema());
  }
}
