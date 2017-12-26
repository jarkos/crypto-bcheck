package pl.jarkos.backend.mail


import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class JavaMailSender(private val host: String, private val port: String) {

    private val props: Properties = System.getProperties()

    fun send(from: String, password: String, to: String, cc: String?, title: String, text: String): Boolean {
        var result: Boolean
        try {

            props.setProperty("mail.smtp.host", this.host)
            props.setProperty("mail.smtp.port", this.port)
            props.put("mail.smtp.auth", "true")
            props.put("mail.debug", "false")
            props.put("mail.store.protocol", "pop3")
            props.put("mail.transport.protocol", "smtp")
            props.put("mail.smtp.starttls.enable", "true")

            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(from, password)
                }
            })

            //            session.setDebug(debug);
            val msg = MimeMessage(session)
            msg.setFrom(InternetAddress(from))

            val addressTO = arrayOf(InternetAddress(to))
            msg.setRecipients(Message.RecipientType.TO, addressTO)

            if (cc != null) {
                val addressCC = arrayOf(InternetAddress(cc))
                msg.setRecipients(Message.RecipientType.CC, addressCC)
            }

            val addressFROM = InternetAddress(from)
            msg.setFrom(addressFROM)

            msg.sentDate = Date()

            msg.subject = title
            msg.setText(text)

            Transport.send(msg)
            result = true
        } catch (ex: Exception) {
            ex.printStackTrace()
            result = false
        }

        return result
    }

    companion object {

        fun sendMail(msg: String) {

            println("Sending notification email... ")
            val host = "smtp.gmail.com"
            val port = "587"

            val sender = JavaMailSender(host, port)

            /** Activate this line for proxy authentication and change the settings with your details  */
            // sender.addProxy("10.10.10.10", "8080");

            /** Activate this line if you need to see more details  */
            //        sender.setDebug(true);

            val from = "kostrzewa.jaroslaw@gmail.com"
            val password = "(led)Orf"

            val to = "jareq92@gmail.com"
            //        String cc = "example3@yahoo.com";

            val subject = "test"

            println()
            val b = sender.send(from, password, to, null, subject, msg)
            if (b) {
                println("Message sent successfully.")
            } else {
                println("Message failed.")
            }
        }
    }
}