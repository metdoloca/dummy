package codec

/**
 * Created by shkim on 15. 6. 29.
 */
class HeaderDefine {

  var isUnsigned:Boolean = true
  var isPacketSizeIncludeHeader=false

  var headerSize = 2
  // packetsize
  var beginDataSizeOffset=0;
  var typeOfDataSize=HeaderDefine.SIZE_SHORT;
  // protocol
  var beginProtocolOffset=0;
  var typeOfProtocolSize=HeaderDefine.SIZE_SHORT;
  var byteOrderSwapInHeader:Boolean = false


}

object HeaderDefine{
  final val SIZE_INT:Int = 4
  final val SIZE_SHORT:Int = 2
  final val SIZE_BYTE:Int = 1
}
