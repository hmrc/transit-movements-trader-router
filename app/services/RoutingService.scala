package services

import connectors.DestinationConnector
import javax.inject.Inject
import play.api.mvc.Headers
import uk.gov.hmrc.http.HttpResponse

import scala.xml.NodeSeq

class RoutingService @Inject()(destinationConnector: DestinationConnector, departureConnector: DepartureConnector) {

    def sendMessage(messageRecipient: String, messageBody: NodeSeq, headers: Headers): HttpResponse = ???

}
