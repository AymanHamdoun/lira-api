package lira.ahamdoun.utility

import redis.clients.jedis.Client
import redis.clients.jedis.JedisPool

class Redis {

    companion object {
        private val pool = JedisPool("localhost", 6379)

        fun getClient(): Client {
            return pool.resource.client
        }
    }

}