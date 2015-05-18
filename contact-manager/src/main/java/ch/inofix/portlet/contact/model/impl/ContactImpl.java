package ch.inofix.portlet.contact.model.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ch.inofix.portlet.contact.dto.AddressDTO;
import ch.inofix.portlet.contact.dto.EmailDTO;
import ch.inofix.portlet.contact.dto.ExpertiseDTO;
import ch.inofix.portlet.contact.dto.HobbyDTO;
import ch.inofix.portlet.contact.dto.ImppDTO;
import ch.inofix.portlet.contact.dto.InterestDTO;
import ch.inofix.portlet.contact.dto.NoteDTO;
import ch.inofix.portlet.contact.dto.PhoneDTO;
import ch.inofix.portlet.contact.dto.StructuredNameDTO;
import ch.inofix.portlet.contact.dto.UriDTO;
import ch.inofix.portlet.contact.dto.UrlDTO;

import com.liferay.portal.kernel.util.Validator;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.ExpertiseLevel;
import ezvcard.parameter.HobbyLevel;
import ezvcard.parameter.ImppType;
import ezvcard.parameter.InterestLevel;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Anniversary;
import ezvcard.property.Birthday;
import ezvcard.property.CalendarRequestUri;
import ezvcard.property.CalendarUri;
import ezvcard.property.Deathdate;
import ezvcard.property.Email;
import ezvcard.property.Expertise;
import ezvcard.property.FormattedName;
import ezvcard.property.Hobby;
import ezvcard.property.Impp;
import ezvcard.property.Interest;
import ezvcard.property.Kind;
import ezvcard.property.Nickname;
import ezvcard.property.Note;
import ezvcard.property.Organization;
import ezvcard.property.Role;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.Timezone;
import ezvcard.property.Title;
import ezvcard.property.Url;

/**
 * The extended model implementation for the Contact service. Represents a row
 * in the &quot;Inofix_Contact&quot; database table, with each column mapped to
 * a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class.
 * Whenever methods are added, rerun ServiceBuilder to copy their definitions
 * into the {@link ch.inofix.portlet.contact.model.Contact} interface.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @author Christian Berndt
 * @created 2015-05-07 22:17
 * @modified 2015-05-18 22:57
 * @version 1.0.1
 */
@SuppressWarnings("serial")
public class ContactImpl extends ContactBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this class directly. All methods that expect a contact
	 * model instance should use the {@link
	 * ch.inofix.portlet.contact.model.Contact} interface instead.
	 */

	public ContactImpl() {
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<AddressDTO> getAddresses() {

		List<AddressDTO> addressDTOs = new ArrayList<AddressDTO>();

		List<Address> addresses = getVCard().getAddresses();

		for (Address address : addresses) {

			AddressDTO addressDTO = new AddressDTO();

			addressDTO.setCountry(address.getCountry());
			addressDTO.setLabel(address.getLabel());
			addressDTO.setLanguage(address.getLanguage());
			addressDTO.setLocality(address.getLocality());
			addressDTO.setPoBox(address.getPoBox());
			addressDTO.setPostalCode(address.getPostalCode());
			addressDTO.setRegion(address.getRegion());
			addressDTO.setStreetAddress(address.getStreetAddress());
			addressDTO.setTimezone(address.getTimezone());

			// TODO: Add multi-type support
			StringBuilder sb = new StringBuilder();
			Set<AddressType> types = address.getTypes();
			if (types.size() > 0) {
				for (AddressType type : types) {
					sb.append(type.getValue());
				}
			} else {
				sb.append("other");
			}

			addressDTO.setType(sb.toString());

			addressDTOs.add(addressDTO);

		}

		return addressDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getAnniversaryDay() {

		Anniversary anniversary = getVCard().getAnniversary();

		if (anniversary != null) {
			Date date = anniversary.getDate();
			return getDay(date);
		} else {
			return 1;
		}

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getAnniversaryMonth() {

		Anniversary anniversary = getVCard().getAnniversary();

		if (anniversary != null) {
			Date date = anniversary.getDate();
			return getMonth(date);
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getAnniversaryYear() {

		Anniversary anniversary = getVCard().getAnniversary();

		if (anniversary != null) {
			Date date = anniversary.getDate();
			return getYear(date);
		} else {
			return 1970;
		}
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getBirthdayDay() {

		Birthday birthday = getVCard().getBirthday();

		if (birthday != null) {
			Date date = birthday.getDate();
			return getDay(date);
		} else {
			return 1;
		}

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getBirthdayMonth() {

		Birthday birthday = getVCard().getBirthday();

		if (birthday != null) {
			Date date = birthday.getDate();
			return getMonth(date);
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getBirthdayYear() {

		Birthday birthday = getVCard().getBirthday();

		if (birthday != null) {
			Date date = birthday.getDate();
			return getYear(date);
		} else {
			return 1970;
		}
	}

	/**
	 * @since 1.0.0
	 */
	public List<UriDTO> getCalendarRequestUris() {

		List<UriDTO> uriDTOs = new ArrayList<UriDTO>();

		List<CalendarRequestUri> calendarRequestUris = getVCard()
				.getCalendarRequestUris();

		for (CalendarRequestUri calendarRequestUri : calendarRequestUris) {

			UriDTO uriDTO = new UriDTO();

			uriDTO.setUri(calendarRequestUri.getValue());
			uriDTO.setType(calendarRequestUri.getType());

			uriDTOs.add(uriDTO);
		}

		return uriDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<UriDTO> getCalendarUris() {

		List<UriDTO> uriDTOs = new ArrayList<UriDTO>();

		List<CalendarUri> calendarUris = getVCard().getCalendarUris();

		for (CalendarUri calendarUri : calendarUris) {

			UriDTO uriDTO = new UriDTO();

			uriDTO.setUri(calendarUri.getValue());
			uriDTO.setType(calendarUri.getType());

			uriDTOs.add(uriDTO);
		}

		return uriDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getCompany() {

		String str = "";

		List<Organization> organizations = getVCard().getOrganizations();

		if (organizations.size() > 0) {
			List<String> values = organizations.get(0).getValues();
			if (values.size() > 0) {
				str = values.get(0);
			}
		}

		return str;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getDeathdateDay() {

		Deathdate deathdate = getVCard().getDeathdate();

		if (deathdate != null) {
			Date date = deathdate.getDate();
			return getDay(date);
		} else {
			return 1;
		}

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getDeathdateMonth() {

		Deathdate deathdate = getVCard().getDeathdate();

		if (deathdate != null) {
			Date date = deathdate.getDate();
			return getMonth(date);
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public int getDeathdateYear() {

		Deathdate deathdate = getVCard().getDeathdate();

		if (deathdate != null) {
			Date date = deathdate.getDate();
			return getYear(date);
		} else {
			return 1970;
		}
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getDepartment() {

		String str = "";

		List<Organization> organizations = getVCard().getOrganizations();

		if (organizations.size() > 0) {
			List<String> values = organizations.get(0).getValues();
			if (values.size() > 1) {
				str = values.get(1);
			}
		}

		return str;

	}

	/**
	 * @param type
	 * @since 1.0.0
	 */
	public String getEmail(EmailType type) {

		String str = "";

		List<Email> emails = getVCard().getEmails();

		for (Email email : emails) {
			Set<EmailType> types = email.getTypes();
			for (EmailType emailType : types) {
				if (emailType.equals(type)) {
					str = email.getValue();
				}
			}
		}

		return str;

	}

	/**
	 * @since 1.0.0
	 */
	public String getEmailHome() {
		return getEmail(EmailType.HOME);
	}

	/**
	 * @since 1.0.0
	 */
	public String getEmailWork() {
		return getEmail(EmailType.WORK);
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<EmailDTO> getEmails() {

		List<EmailDTO> emailDTOs = new ArrayList<EmailDTO>();

		List<Email> emails = getVCard().getEmails();

		for (Email email : emails) {

			EmailDTO emailDTO = new EmailDTO();

			emailDTO.setAddress(email.getValue());

			// TODO: Add multi-type support
			StringBuilder sb = new StringBuilder();
			Set<EmailType> types = email.getTypes();
			if (types.size() > 0) {
				for (EmailType type : types) {
					sb.append(type.getValue());
				}
			} else {
				sb.append("other");
			}

			emailDTO.setType(sb.toString());

			emailDTOs.add(emailDTO);
		}

		return emailDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<ExpertiseDTO> getExpertises() {

		List<Expertise> expertises = getVCard().getExpertise();
		List<ExpertiseDTO> expertiseDTOs = new ArrayList<ExpertiseDTO>();

		for (Expertise expertise : expertises) {
			ExpertiseDTO expertiseDTO = new ExpertiseDTO();
			expertiseDTO.setValue(expertise.getValue());
			ExpertiseLevel level = expertise.getLevel();
			if (level != null) {
				expertiseDTO.setLevel(level.getValue());
			}
			expertiseDTOs.add(expertiseDTO);
		}

		return expertiseDTOs;
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getFamily() {

		String family = "";

		StructuredName sn = getVCard().getStructuredName();

		if (sn != null) {
			family = sn.getFamily();
		}

		return family;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getFormattedName() {

		String formattedName = "";

		FormattedName fn = getVCard().getFormattedName();

		if (fn != null) {
			formattedName = fn.getValue();
		}

		return formattedName;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getFullName() {
		return getFullName(false);
	}

	/**
	 * 
	 * @param firstLast
	 * @return
	 * @since 1.0.0
	 */
	public String getFullName(boolean firstLast) {

		StringBuilder sb = new StringBuilder();

		StructuredName sn = getVCard().getStructuredName();

		if (sn != null) {
			if (firstLast) {
				sb.append(sn.getGiven());
				sb.append(" ");
				sb.append(sn.getFamily());
			} else {
				sb.append(sn.getFamily());
				sb.append(", ");
				sb.append(sn.getGiven());
			}
		}

		return sb.toString();

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getGiven() {

		String given = "";

		StructuredName sn = getVCard().getStructuredName();

		if (sn != null) {
			given = sn.getGiven();
		}

		return given;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<HobbyDTO> getHobbies() {

		List<Hobby> hobbies = getVCard().getHobbies();
		List<HobbyDTO> hobbyDTOs = new ArrayList<HobbyDTO>();

		for (Hobby hobby : hobbies) {
			HobbyDTO hobbyDTO = new HobbyDTO();
			hobbyDTO.setValue(hobby.getValue());
			HobbyLevel level = hobby.getLevel();
			if (level != null) {
				hobbyDTO.setLevel(level.getValue());
			}
			hobbyDTOs.add(hobbyDTO);
		}

		return hobbyDTOs;
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<ImppDTO> getImpps() {

		List<ImppDTO> imppDTOs = new ArrayList<ImppDTO>();

		List<Impp> impps = getVCard().getImpps();

		for (Impp impp : impps) {

			ImppDTO imppDTO = new ImppDTO();

			StringBuilder sb = new StringBuilder();

			Set<ImppType> types = impp.getTypes();

			// TODO: Add support for multiple types e.g.
			// home-skype, work-jabber, etc.
			if (types.size() > 0) {
				for (ImppType type : types) {
					sb.append(type.getValue());
				}
			} else {
				sb.append("other");
			}

			imppDTO.setProtocol(impp.getProtocol());
			imppDTO.setType(sb.toString());

			String protocol = impp.getProtocol();
			String uri = impp.getUri().toString();

			// TODO: find a cleaner solution for this
			uri = uri.replace(protocol + ":", "");

			imppDTO.setUri(uri);

			imppDTOs.add(imppDTO);
		}

		return imppDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getKind() {

		String str = "individual";

		Kind kind = getVCard().getKind();

		if (kind != null) {
			str = kind.getValue();
		}

		return str;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<InterestDTO> getInterests() {

		List<Interest> interests = getVCard().getInterests();
		List<InterestDTO> interestDTOs = new ArrayList<InterestDTO>();

		for (Interest interest : interests) {
			InterestDTO interestDTO = new InterestDTO();
			interestDTO.setValue(interest.getValue());
			InterestLevel level = interest.getLevel();
			if (level != null) {
				interestDTO.setLevel(level.getValue());
			}
			interestDTOs.add(interestDTO);
		}

		return interestDTOs;
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getNickname() {

		StringBuilder sb = new StringBuilder();

		Nickname nickname = getVCard().getNickname();

		if (nickname != null) {
			List<String> names = nickname.getValues();
			for (String name : names) {
				sb.append(name);
			}
		}

		return sb.toString();

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<NoteDTO> getNotes() {

		List<Note> notes = getVCard().getNotes();
		List<NoteDTO> noteDTOs = new ArrayList<NoteDTO>();

		for (Note note : notes) {
			NoteDTO noteDTO = new NoteDTO();
			noteDTO.setValue(note.getValue());
			noteDTOs.add(noteDTO);
		}

		return noteDTOs;
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getOffice() {

		String str = "";

		List<Organization> organizations = getVCard().getOrganizations();

		if (organizations.size() > 0) {
			List<String> values = organizations.get(0).getValues();
			if (values.size() > 2) {
				str = values.get(2);
			}
		}

		return str;

	}

	/**
	 * 
	 * @param type
	 * @return
	 * @since 1.0.0
	 */
	public String getPhone(TelephoneType type) {

		String str = "";

		List<Telephone> telephones = getVCard().getTelephoneNumbers();

		for (Telephone telephone : telephones) {
			Set<TelephoneType> types = telephone.getTypes();
			for (TelephoneType telephoneType : types) {
				if (telephoneType.equals(type)) {
					str = telephone.getText();
				}
			}
		}

		return str;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getPhoneHome() {
		return getPhone(TelephoneType.HOME);
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getPhoneMobile() {
		return getPhone(TelephoneType.CELL);
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getPhoneWork() {
		return getPhone(TelephoneType.WORK);
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<PhoneDTO> getPhones() {

		List<PhoneDTO> phoneDTOs = new ArrayList<PhoneDTO>();

		List<Telephone> phones = getVCard().getTelephoneNumbers();

		for (Telephone phone : phones) {

			PhoneDTO phoneDTO = new PhoneDTO();

			phoneDTO.setNumber(phone.getText());

			StringBuilder sb = new StringBuilder();

			Set<TelephoneType> types = phone.getTypes();

			// TODO: Add support for multiple telephone types
			// e.g. home-fax, work-mobile, etc.
			if (types.size() > 0) {
				for (TelephoneType type : types) {
					sb.append(type.getValue());
				}
			} else {
				sb.append("other");
			}

			phoneDTO.setType(sb.toString());

			phoneDTOs.add(phoneDTO);
		}

		return phoneDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getRole() {

		StringBuilder sb = new StringBuilder();

		// TODO: How must we handle multiple roles?
		List<Role> roles = getVCard().getRoles();
		if (roles != null) {
			for (Role role : roles) {
				sb.append(role.getValue());
			}
		}

		return sb.toString();
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getSortAs() {

		StringBuilder sb = new StringBuilder();

		StructuredName sn = getVCard().getStructuredName();

		if (sn != null) {
			List<String> list = sn.getSortAs();
			for (String str : list) {
				sb.append(str);
			}
		}

		return sb.toString();

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public StructuredNameDTO getStructuredName() {

		StructuredNameDTO structuredNameDTO = new StructuredNameDTO();
		StructuredName sn = getVCard().getStructuredName();

		if (sn != null) {

			StringBuilder sb = new StringBuilder();

			List<String> additionals = sn.getAdditional();
			for (String additional : additionals) {
				sb.append(additional);
			}

			String additional = sb.toString();

			sb = new StringBuilder();

			List<String> prefixes = sn.getSuffixes();
			for (String prefix : prefixes) {
				sb.append(prefix);
			}

			String prefix = sb.toString();

			sb = new StringBuilder();

			List<String> suffixes = sn.getSuffixes();
			for (String suffix : suffixes) {
				sb.append(suffix);
			}

			String suffix = sb.toString();

			structuredNameDTO.setAdditional(additional);
			structuredNameDTO.setFamily(sn.getFamily());
			structuredNameDTO.setGiven(sn.getGiven());
			// structuredNameDTO.setGroup(sn.getGroup());
			structuredNameDTO.setPrefix(prefix);
			structuredNameDTO.setSuffix(suffix);

		}

		return structuredNameDTO;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getTitle() {

		StringBuilder sb = new StringBuilder();

		// TODO: How must we handle multiple titles?
		List<Title> titles = getVCard().getTitles();
		if (titles != null) {
			for (Title title : titles) {
				sb.append(title.getValue());
			}
		}

		return sb.toString();
	}

	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getTimezone() {

		String str = "";
		Timezone timezone = getVCard().getTimezone();

		if (timezone != null) {
			str = timezone.getText();
		}

		return str;
	}

	/**
	 * @since 1.0.0
	 */
	public VCard getVCard() {

		String str = getCard();
		VCard vCard = null;

		if (Validator.isNotNull(str)) {
			vCard = Ezvcard.parse(str).first();
		} else {
			vCard = new VCard();
		}

		return vCard;

	}

	/**
	 * @since 1.0.0
	 */
	public String getVCardHTML() {

		VCard vCard = getVCard();
		List<VCard> vcards = new ArrayList<VCard>();
		vcards.add(vCard);
		return Ezvcard.writeHtml(vcards).go();

	}

	/**
	 * @since 1.0.0
	 */
	public List<UrlDTO> getUrls() {

		List<UrlDTO> urlDTOs = new ArrayList<UrlDTO>();

		List<Url> urls = getVCard().getUrls();

		for (Url url : urls) {

			UrlDTO urlDTO = new UrlDTO();

			urlDTO.setAddress(url.getValue());
			urlDTO.setType(url.getType());

			urlDTOs.add(urlDTO);
		}

		return urlDTOs;

	}

	/**
	 * 
	 * @param date
	 * @return
	 * @since 1.0.0
	 */
	private int getDay(Date date) {

		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.get(Calendar.DAY_OF_MONTH);
		} else {
			return 1;
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 * @since 1.0.0
	 */
	private int getMonth(Date date) {

		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.get(Calendar.MONTH);
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 * @since 1.0.0
	 */
	private int getYear(Date date) {

		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.get(Calendar.YEAR);
		} else {
			return 1970;
		}
	}

}
