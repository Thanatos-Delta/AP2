import kotlin.Exception


//1.1
class Time constructor (hour : Int , minute : Int ){

    //val da unveränderlich gefordert war
    internal val hour = hour
    internal val minute = minute

    //1.2 anstatt im Konstruktor Abfrage für gültige Werte über init-Block implementiert
    init {
        if(hour < 0 || hour > 24){
            throw Exception("Invalid time!")

        }
        if(minute < 0 || minute > 60){
            throw Exception("Invalid time!")
        }

    }

}
class InvalidCheckoutTime : Exception ()
class CheckoutTimeNotSet : Exception ()

//2
// Das ? hinter dem Datentyp implementiert die Nullfähigkeit
//Alt mit Konstruktor Time? für Nullfähigkeit
//class ParkTicket constructor (entryTime : Time , exitTime : Time? = null){

//Anpassung für Aufgabe 5 Time? für Nullfähigkeit
class ParkTicket constructor (entryTime : Time , exitTime : Time? = null){

    val entryTime = entryTime
    // set Methode für exitTime auf private, get ist weiterhin public sonst Zugriff in String-Template wie vorgegeben nicht möglich
    var exitTime = exitTime
        private set

    //exitTime gesetzt?
    fun exitTimeNull() : Boolean = this.exitTime == null

    //Check ob entryTime vor exitTime
    fun validCheckout(entryTime: Time , exitTime: Time) : Boolean {

        if(entryTime.hour < exitTime.hour){
            return true
        }
        else return entryTime.hour == exitTime.hour && entryTime.minute <= exitTime.minute
    }

    //2.2
    fun checkout(exitTime : Time){
        if(validCheckout(entryTime , exitTime))
            this.exitTime = exitTime
        else{
            throw InvalidCheckoutTime()
        }
   }

    //2.3
    /*
    fun parkingDuration() : Int{
        if(exitTimeNull){
            throw CheckoutTimeNotSet()
        }
        val entryToMinute = this.entryTime.hour * 60 + this.entryTime.minute
        val exitToMinute = this.exitTime!!.hour * 60 + this.exitTime!!.minute

        return exitToMinute - entryToMinute
    }

    fun hoursStarted() : Int = if(parkingDuration() % 60 == 0) parkingDuration() / 60 else parkingDuration() /60 +1
    */

    //2.4
    // Generell werden Eigenschaften eines Objektes, die seinen Zustand beschreiben, mit Variablen implementiert und Aktionen eines Objektes mittels Methoden.
    // Was die "parkingDuration" und "hoursStarted" angeht, würde ich sagen, das es am meisten Sinn macht sie als Zustandsbeschreibung zu sehen und somit mittels Variablen zu implementieren. Durch die set-Methode können diese schliesslich auch dynamisch gehalten werden.
    internal var parkingDuration = 0
    get(){

        if(exitTimeNull()){
            throw CheckoutTimeNotSet()
        }
        val entryToMinute = this.entryTime.hour * 60 + this.entryTime.minute
        val exitToMinute = this.exitTime!!.hour * 60 + this.exitTime!!.minute
        return exitToMinute - entryToMinute
    }


    internal var hoursStarted = 0
    get(){
        return if(parkingDuration % 60 == 0) parkingDuration / 60 else parkingDuration /60 +1
    }

}

//3.2
enum class Tarif{
    STANDARD,
    EVENT,
    WEEKEND;
    fun price() : Double{
        return when(this){
            STANDARD -> 1.99
            EVENT -> 1.49
            WEEKEND -> 2.99


        }
    }

}

//4.2
class TicketMachine (tarif : Tarif){
    private val ticketList : MutableList<ParkTicket> = mutableListOf()
    private val validTicketList : MutableList<ParkTicket> = mutableListOf()
    private val tarif = tarif

    fun generate(entryTime: Time) : ParkTicket{
        val ticket : ParkTicket =  ParkTicket(entryTime)
        ticketList.add(ticket)
        return ticket
    }

    fun validTickets() : MutableList<ParkTicket>{
        for(tickets in this.ticketList) {
            if (!tickets.exitTimeNull()) {
                this.validTicketList.add(tickets)
            }
        }
        return validTicketList
    }

    fun shortestParkingDuration() : Int {
        val sortedTickets = validTicketList.sortedBy { it.parkingDuration }
        return sortedTickets.first().parkingDuration
    }
    fun averageParkingDuration() : Int {
        var averageTime = 0
        var counter = 0

        for(tickets in this.validTicketList){
            averageTime += tickets.parkingDuration
            counter++
        }

        return averageTime / counter
    }

    fun revenues() : Double{
        var revenuesTotal : Double = 0.0
        for (tickets in this.validTicketList){
            revenuesTotal += tickets.hoursStarted * tarif.price()
        }
        return revenuesTotal
    }



}






fun main() {

    /*
    //"Testcode" aus 1.2

    val time1 = Time(12, 0) // valide Uhrzeit
    val time2 = Time(12, 30) // valide Uhrzeit
    val time3 = Time(26, 120) // invalide Uhrzeit. Programm stürzt mit einer Fehlermeldung (Exception) ab.
    val time4 = Time(-5, -10)// invalide Uhrzeit. Programm stürzt mit einer Fehlermeldung (Exception) ab.

     */

    // "Testcode" aus 2.2
    val entryTime = Time(12, 0)
    val ticket : ParkTicket = ParkTicket(entryTime)
try {
    ticket.checkout(Time(12, 30))
    ticket.checkout(Time(11, 0)) // funktioniert nicht. Parkdauer kann nicht negativ sein. Programm stürzt mit einer Fehlermeldung (Exception) ab.
} catch ( ex : InvalidCheckoutTime){
    println("Ausfahrtszeit ${ticket.exitTime} liegt vor der Einfahrtszeit ${ticket.entryTime}")

}

    //2.3
    /*
    try {
        println(ticket.parkingDuration()) // Gibt 30 aus
        println(ticket.hoursStarted()) // Gibt 1 aus
    } catch (ex : CheckoutTimeNotSet){
        println("Parkticket muss vorher korrekt abgestempelt werden!")
    }

    val ticket2 = ParkTicket(Time(12, 30))
    ticket2.checkout(Time(13, 40)) // funktioniert, weil 12:30 < 13:40
    println(ticket2.parkingDuration()) // Gibt 70 aus
    println(ticket2.hoursStarted()) // Gibt 2 aus
*/
    //2.4
    try {
        println(ticket.parkingDuration) // Gibt 30 aus
        println(ticket.hoursStarted) // Gibt 1 aus
    } catch (ex : CheckoutTimeNotSet){
        println("Parkticket muss vorher korrekt abgestempelt werden!")
    }

    val ticket_2 = ParkTicket(Time(12, 30))
    ticket_2.checkout(Time(13, 40)) // funktioniert, weil 12:30 < 13:40
    println(ticket_2.parkingDuration) // Gibt 70 aus
    println(ticket_2.hoursStarted) // Gibt 2 aus


    // 3.2
    val default = Tarif.STANDARD
    println(default.price()) // Gibt 1.99 aus

    val weekend = Tarif.WEEKEND
    println(weekend.price()) // Gibt 2.99 aus




    //4.4
    val machine = TicketMachine(Tarif.STANDARD)
    val ticket1 = machine.generate(Time(12, 0))
    val ticket2 = machine.generate(Time(12, 30))
    val ticket3 = machine.generate(Time(13, 30))
    val ticket4 = machine.generate(Time(13, 30))

    ticket1.checkout(Time(12, 30)) // 30 min (1h)
    ticket2.checkout(Time(13, 30)) // 60 min (1h)
    ticket3.checkout(Time(14, 50)) // 80 min (2h)

    machine.validTickets()

    println(machine.shortestParkingDuration()) // Gibt 30 aus
    println(machine.averageParkingDuration()) // Gibt 56 aus ((30 + 60 + 80) / 3)
    println(machine.revenues()) // Gibt 7.96 aus ((1 + 1 + 2) * machine.tarif.price())


    //"Tests" für 5
    //val machine = TicketMachine(Tarif.STANDARD)
    val ticket5 = machine.generate(Time(12, 0))
    //ticket1.exitTime = Time(10, 0) // exitTime direkt setzen, ohne checkout aufzurufen
}


