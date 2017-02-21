package ch.inofix.portlet.payment.util;

/**
 * 
 * @author Christian Berndt
 * @created 2017-02-16 18:16
 * @modified 2017-02-21 17:47
 * @version 1.0.1
 *
 */
public class PaymentConstants {

    public static final String[] BILLING_ADDRESS_PARAMETERS = new String[] {
            "address", "address2", "city", "postal_code", "state", "country",
            "first_name", "last_name", "email", "phone" };

    public static final String[] COMMON_PARAMETERS = new String[] {
            "payment_type", "api_key", "order_id", "merchant _reference",
            "amount", "shipping_costs", "vat", "currency", "postback_url",
            "customer_ip", "customer_ip_proxy", "original_transaction_id",
            "duration", "locale", "checksum" };

    public static final String[] SERVICE_KEYS = new String[] {
            "credit-card-redirect", "credit-card-inline", "direct-debit",
            "purchase-on-account-b2c", "purchase-on-account-b2b", "giro-pay",
            "sofort-ueberweisung", "paypal", "pay-direkt" };

    public static final String[] SHIPPING_ADDRESS_PARAMETERS = new String[] {
            "shipping_address", "shipping_address2", "shipping_company",
            "shipping_city", "shipping_postal_code", "shipping_state",
            "shipping_country", "shipping_first_name", "shipping_last_name" };

}
