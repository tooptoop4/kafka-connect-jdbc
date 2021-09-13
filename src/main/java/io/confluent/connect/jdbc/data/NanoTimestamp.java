/*
 * Copyright 2021 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.connect.jdbc.data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

/**
 * Utility class for managing nano timestamp representations
 * 
 */
public class NanoTimestamp {
  public static final String SCHEMA_NAME = "io.confluent.connect.jdbc.data.NanoTimestamp";
  static final long NANOSECONDS_PER_SECOND = TimeUnit.SECONDS.toNanos(1);
  static final long NANOSECONDS_PER_MILLISECOND = TimeUnit.MILLISECONDS.toNanos(1);
  static final long NULL_TIMESTAMP = -1;

  public static SchemaBuilder builder() {
    return SchemaBuilder.int64()
                        .name(SCHEMA_NAME)
                        .version(1);
  }

  public static Schema schema() {
    return builder().build();
  }

  public static long toNanoEpoch(Timestamp timestamp) {
    if (timestamp != null) {
      Instant instant = timestamp.toInstant();
      return instant.getEpochSecond() * NANOSECONDS_PER_SECOND + instant.getNano();
    } else {
      return NULL_TIMESTAMP;
    }
  }

  // java.sql.Timestamp constructor only takes milliseconds, which is fine since we
  // we previously truncated to milliseconds anyways
  public static Timestamp fromNanoEpoch(long nanoEpoch) {
    if (nanoEpoch != NULL_TIMESTAMP) {
      return new Timestamp(nanoEpoch / NANOSECONDS_PER_MILLISECOND);
    } else {
      return null;
    }
  }

  private NanoTimestamp() {
  }
}