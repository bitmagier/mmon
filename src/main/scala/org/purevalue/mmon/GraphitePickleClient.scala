package org.purevalue.mmon

import java.io.{OutputStream, OutputStreamWriter, Writer}
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class GraphitePickleClient(socketProvider: () => Socket) {

  case class Metric(name: String, timestamp: Long, value: String)

  /**
   * Minimally necessary pickle opcodes.
   */
  private val MARK: Char = '('
  private val STOP: Char = '.'
  private val LONG: Char = 'L'
  private val STRING: Char = 'S'
  private val APPEND: Char = 'a'
  private val LIST: Char = 'l'
  private val TUPLE: Char = 't'

  /**
   * See: http://readthedocs.org/docs/graphite/en/1.0/feeding-carbon.html
   */
  private def pickleMetrics(metrics: List[Metric]): String = {
    val pickled: StringBuilder = new StringBuilder
    pickled.append(MARK)
    pickled.append(LIST)
    import scala.collection.JavaConversions._
    for (tuple <- metrics) { // start the outer tuple
      pickled.append(MARK)
      // the metric name is a string.
      pickled.append(STRING)
      // the single quotes are to match python's repr("abcd")
      pickled.append('\'')
      pickled.append(tuple.name)
      pickled.append('\'')
      pickled.append('\n')
      // start the inner tuple
      pickled.append(MARK)
      // timestamp is a long
      pickled.append(LONG)
      pickled.append(tuple.timestamp)
      // the trailing L is to match python's repr(long(1234))
      pickled.append('L')
      pickled.append('\n')
      // and the value is a string.
      pickled.append(STRING)
      pickled.append('\'')
      pickled.append(tuple.value)
      pickled.append('\'')
      pickled.append('\n')
      pickled.append(TUPLE) // inner close

      pickled.append(TUPLE) // outer close

      pickled.append(APPEND)
    }
    // every pickle ends with STOP
    pickled.append(STOP)
    pickled.toString
  }

  def writeMetrics(metrics: List[Metric]): Unit = {
    val payload = pickleMetrics(metrics)

    val length: Int = payload.length
    val header: Array[Byte] = ByteBuffer.allocate(4).putInt(length).array

    var socket: Socket = null
    try {
      socket = socketProvider.apply()
      val out: OutputStream = socket.getOutputStream
      out.write(header)
      val pickleWriter: Writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)
      pickleWriter.write(payload)
      pickleWriter.flush()
    } finally {
      if (socket != null) {
        socket.shutdownOutput()
        socket.close()
      }
    }
  }
}

// TODO rewrite in pure scala