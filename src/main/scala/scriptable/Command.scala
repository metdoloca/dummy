package scriptable

case class Command(op:String,fileName:String, code:String, hostIp:String, sessionKey:String)
case class LogLine(line:String,lineNumber:Int, sessionKey:String)