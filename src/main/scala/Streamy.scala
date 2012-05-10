
package com.github.bigtoast.streamy

import unfiltered.request._
import unfiltered.response._
import unfiltered.netty._
import akka.actor.{ ActorSystem, Actor, Props }
import akka.util.duration._
import java.util.concurrent.atomic.AtomicInteger
import org.jboss.netty.channel._
import org.jboss.netty.handler.codec.http.{ HttpHeaders, DefaultHttpChunk }
import org.jboss.netty.buffer.ChannelBuffers

class StreamHandler extends Actor {
  
  var req :Option[HttpRequest[ReceivedMessage]] = None
  
  def channel = req map { _.underlying.event.getChannel }
  
  val counter = new AtomicInteger
  
  case object Tick
  
  val text = TransferEncoding(HttpHeaders.Values.CHUNKED) ~> PlainTextContent
  
  
  def receive = {
    case req :HttpRequest[ReceivedMessage] =>
      this.req = Some( req )
      val start = req.underlying.defaultResponse( text )
      channel.foreach( _.write(start) addListener listener )
      context.system.scheduler.schedule( 0 seconds , 1 seconds, self, Tick ) 
      
    case Tick =>
      channel.foreach( _.write( chunk( "Streamy says hi for the %d time \n" format counter.incrementAndGet  ) ) addListener listener )
      
    case future :ChannelFuture =>
      if ( future.getCause() != null )
        context.stop( self )
  }
    
  def chunk( msg :String ) = {
     new DefaultHttpChunk( ChannelBuffers.copiedBuffer( msg.getBytes("utf-8") ) )
  }
  
  val listener = new ChannelFutureListener {
    def operationComplete( future :ChannelFuture ) = self ! future
  }
  
  override def postStop = channel.foreach { c => 
    c.close() 
    println("Client disconnect. Channel closed. Actor stopped") 
  }
  
}

object StreamyPlan extends async.Plan {

  val system = ActorSystem("StreamySystem")
  
  var resp = Nil
  
  override def intent = {
    case req @ GET( _ ) =>
      val actor = system.actorOf( Props[StreamHandler] )
      actor ! req    
  }
  
  override def onException( context : ChannelHandlerContext, t :Throwable ) = Unit
  
}

object Streamy extends App {

  val port = args.length == 1 match {
    case true => args( 0 ) toInt 
    case _ => 6666
  }
  
  println("Starting streamy on port %d" format port )
  
  Http( port ).handler( StreamyPlan ).beforeStop {
    println( "Piece out.." )
    } run
  
}