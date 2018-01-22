package pl.jarkos.backend.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Component
open class JavaMailSender {

    @Value("\${email.client.password}")
    lateinit var password: String
    @Value("\${email.client.from}")
    lateinit var from: String
    @Value("\${email.to}")
    lateinit var to: String
    val host = "smtp.gmail.com"
    val port = "587"
    private val props: Properties = System.getProperties()

    fun send(title: String, text: String): Boolean {
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

            val msg = MimeMessage(session)
            msg.setFrom(InternetAddress(from))

            val addressTO = arrayOf(InternetAddress(to))
            msg.setRecipients(Message.RecipientType.TO, addressTO)
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

    fun sendMail(msg: String) {
        println("Sending notification email... ")
        val sender = JavaMailSender()
        val subject = "test"
        val b = sender.send(subject, msg)
        if (b) {
            println("Message sent successfully.")
        } else {
            println("Message failed.")
        }
    }
}