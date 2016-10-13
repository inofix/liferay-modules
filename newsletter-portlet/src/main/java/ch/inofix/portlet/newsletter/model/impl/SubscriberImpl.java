package ch.inofix.portlet.newsletter.model.impl;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * The extended model implementation for the Subscriber service. Represents a
 * row in the &quot;inofix_Subscriber&quot; database table, with each column
 * mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class.
 * Whenever methods are added, rerun ServiceBuilder to copy their definitions
 * into the {@link ch.inofix.portlet.newsletter.model.Subscriber} interface.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-10-13 16:31
 * @modied 2016-10-13 16:31
 * @version 1.0.0
 */
public class SubscriberImpl extends SubscriberBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. All methods that expect a subscriber
     * model instance should use the {@link
     * ch.inofix.portlet.newsletter.model.Subscriber} interface instead.
     */
    public SubscriberImpl() {
    }

    /**
     *
     * @return
     * @since 1.0.0
     */
    @Override
    public String getName() {

        if (name == null) {

            StringBuilder sb = new StringBuilder();

            if (Validator.isNotNull(getFirstname())) {
                sb.append(getFirstname());
            }
            if (Validator.isNotNull(getLastname())) {
                if (Validator.isNotNull(sb.toString())) {
                    sb.append(StringPool.BLANK);
                }
                sb.append(getLastname());
            }

            name = sb.toString();
        }

        return name;

    }


    public void setName(String name) {
        this.name = name;
    }

    private String name = null;
}
