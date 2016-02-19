package ch.inofix.portlet.contact.model.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.inofix.portlet.contact.dto.AddressDTO;
import ch.inofix.portlet.contact.dto.CategoriesDTO;
import ch.inofix.portlet.contact.dto.EmailDTO;
import ch.inofix.portlet.contact.dto.ExpertiseDTO;
import ch.inofix.portlet.contact.dto.FileDTO;
import ch.inofix.portlet.contact.dto.HobbyDTO;
import ch.inofix.portlet.contact.dto.ImppDTO;
import ch.inofix.portlet.contact.dto.InterestDTO;
import ch.inofix.portlet.contact.dto.LanguageDTO;
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
import ezvcard.parameter.ImageType;
import ezvcard.parameter.ImppType;
import ezvcard.parameter.InterestLevel;
import ezvcard.parameter.KeyType;
import ezvcard.parameter.SoundType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Anniversary;
import ezvcard.property.Birthday;
import ezvcard.property.Birthplace;
import ezvcard.property.CalendarRequestUri;
import ezvcard.property.CalendarUri;
import ezvcard.property.Categories;
import ezvcard.property.Deathdate;
import ezvcard.property.Deathplace;
import ezvcard.property.Email;
import ezvcard.property.Expertise;
import ezvcard.property.FormattedName;
import ezvcard.property.FreeBusyUrl;
import ezvcard.property.Gender;
import ezvcard.property.Hobby;
import ezvcard.property.Impp;
import ezvcard.property.Interest;
import ezvcard.property.Key;
import ezvcard.property.Kind;
import ezvcard.property.Language;
import ezvcard.property.Logo;
import ezvcard.property.Nickname;
import ezvcard.property.Note;
import ezvcard.property.Organization;
import ezvcard.property.Photo;
import ezvcard.property.Role;
import ezvcard.property.Sound;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.Timezone;
import ezvcard.property.Title;
import ezvcard.property.Url;
import ezvcard.util.DataUri;

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
 * @modified 2015-07-04 14:48
 * @version 1.1.8
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
	 * @param address
	 * @return
	 * @since 1.0.8
	 */
	private AddressDTO getAddress(Address address) {

		AddressDTO addressDTO = new AddressDTO();

		if (address != null) {

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
		}

		return addressDTO;
	}

	/**
	 * 
	 * @return the preferred address.
	 * @since 1.0.8
	 */
	public AddressDTO getAddress() {
		
		List<Address> addresses = getVCard().getAddresses(); 
		
		if (addresses != null) {
			
			for (Address address : addresses) {
				Integer pref = address.getPref(); 
				if (pref != null) {
					if (pref == 1) {
						return getAddress(address); 
					}
				}				
			}			
		}

		Address address = getVCard().getProperty(Address.class);

		return getAddress(address);

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

			AddressDTO addressDTO = getAddress(address);

			addressDTOs.add(addressDTO);

		}

		// an empty default address
		if (addressDTOs.size() == 0) {
			addressDTOs.add(new AddressDTO());
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
	 * 
	 * @return
	 * @since 1.0.5
	 */
	public String getBirthplace() {

		String str = "";

		Birthplace birthplace = getVCard().getBirthplace();

		if (birthplace != null) {
			str = birthplace.getText();
		}

		return str;

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

		// an empty default calendarRequestUri
		if (uriDTOs.size() == 0) {
			uriDTOs.add(new UriDTO());
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

		// an empty default calendarUri
		if (uriDTOs.size() == 0) {
			uriDTOs.add(new UriDTO());
		}

		return uriDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.1.0
	 */
	public List<CategoriesDTO> getCategoriesList() {

		List<CategoriesDTO> categoriesDTOs = new ArrayList<CategoriesDTO>();

		List<Categories> categoriesList = getVCard().getCategoriesList();

		for (Categories categories : categoriesList) {

			CategoriesDTO categoriesDTO = new CategoriesDTO();

			StringBuilder sb = new StringBuilder();
			List<String> values = categories.getValues();
			Iterator<String> iterator = values.iterator();

			while (iterator.hasNext()) {
				sb.append(iterator.next());
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}

			categoriesDTO.setValue(sb.toString());
			categoriesDTO.setType(categories.getType());

			categoriesDTOs.add(categoriesDTO);
		}

		// an empty default categories
		if (categoriesDTOs.size() == 0) {
			categoriesDTOs.add(new CategoriesDTO());
		}

		return categoriesDTOs;

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
	 * 
	 * @return
	 * @since 1.0.5
	 */
	public String getDeathplace() {

		String str = "";

		Deathplace deathplace = getVCard().getDeathplace();

		if (deathplace != null) {
			str = deathplace.getText();
		}

		return str;

	}

	/**
	 * 
	 * @return the preferred email.
	 * @since 1.0.8
	 */
	public EmailDTO getEmail() {
		
		List<Email> emails = getVCard().getEmails(); 
		
		if (emails != null) {
			
			for (Email email : emails) {
				Integer pref = email.getPref(); 
				if (pref != null) {
					if (pref == 1) {
						return getEmail(email); 
					}
				}				
			}			
		}

		Email email = getVCard().getProperty(Email.class);

		return getEmail(email);

	}

	/**
	 * 
	 * @param email
	 * @return
	 * @since 1.0.8
	 */
	private EmailDTO getEmail(Email email) {

		EmailDTO emailDTO = new EmailDTO();

		if (email != null) {
			emailDTO.setAddress(email.getValue());

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
		}

		return emailDTO;
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

			EmailDTO emailDTO = getEmail(email);

			emailDTOs.add(emailDTO);
		}

		// an empty default email
		if (emailDTOs.size() == 0) {
			emailDTOs.add(new EmailDTO());
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

		// an empty default expertise
		if (expertiseDTOs.size() == 0) {
			expertiseDTOs.add(new ExpertiseDTO());
		}

		return expertiseDTOs;
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
	 * @since 1.0.6
	 */
	public List<UrlDTO> getFreeBusyUrls() {

		List<UrlDTO> urlDTOs = new ArrayList<UrlDTO>();

		List<FreeBusyUrl> urls = getVCard().getFbUrls();

		for (FreeBusyUrl url : urls) {

			UrlDTO urlDTO = new UrlDTO();

			urlDTO.setAddress(url.getValue());
			urlDTO.setType(url.getType());

			urlDTOs.add(urlDTO);
		}

		// an empty default freeBusyURL
		if (urlDTOs.size() == 0) {
			urlDTOs.add(new UrlDTO());
		}

		return urlDTOs;

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

		String fullName = sb.toString();

		if (Validator.isNull(fullName)) {

			Organization organization = getVCard().getOrganization();

			if (organization != null) {

				List<String> values = organization.getValues();

				Iterator<String> iterator = values.iterator();

				while (iterator.hasNext()) {

					sb.append(iterator.next());
					if (iterator.hasNext()) {
						sb.append(", ");
					}

				}
			}

		}

		return fullName;

	}

	/**
	 * 
	 * @return
	 * @since 1.1.5
	 */
	public String getGender() {

		String str = Gender.UNKNOWN;

		Gender gender = getVCard().getGender();

		if (gender != null) {
			str = gender.getGender();
		}

		return str;

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

		// an empty default hobby
		if (hobbyDTOs.size() == 0) {
			hobbyDTOs.add(new HobbyDTO());
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

		// an empty default impp
		if (imppDTOs.size() == 0) {
			imppDTOs.add(new ImppDTO());
		}

		return imppDTOs;

	}

	public List<FileDTO> getKeys() {

		List<FileDTO> fileDTOs = new ArrayList<FileDTO>();

		List<Key> keys = getVCard().getKeys();

		for (Key key : keys) {

			FileDTO fileDTO = new FileDTO();
			fileDTO.setUrl(key.getUrl());

			KeyType contentType = key.getContentType();

			if (Validator.isNotNull(contentType)) {
				DataUri dataUri = new DataUri(contentType.getMediaType(),
						key.getData());
				fileDTO.setData(dataUri.toString());
			}

			fileDTOs.add(fileDTO);
		}

		// an empty default key
		if (fileDTOs.size() == 0) {
			fileDTOs.add(new FileDTO());
		}

		return fileDTOs;

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

		// an empty default interest
		if (interestDTOs.size() == 0) {
			interestDTOs.add(new InterestDTO());
		}

		return interestDTOs;
	}

	/**
	 * 
	 * @return
	 * @since 1.1.1
	 */
	public List<LanguageDTO> getLanguages() {

		List<Language> languages = getVCard().getLanguages();
		List<LanguageDTO> languageDTOs = new ArrayList<LanguageDTO>();

		for (Language language : languages) {

			LanguageDTO languageDTO = new LanguageDTO();
			languageDTO.setKey(language.getValue());

			languageDTOs.add(languageDTO);
		}

		return languageDTOs;
	}

	/**
	 * 
	 * @return
	 * @since 1.1.3
	 */
	public List<FileDTO> getLogos() {

		List<FileDTO> fileDTOs = new ArrayList<FileDTO>();

		List<Logo> logos = getVCard().getLogos();

		for (Logo logo : logos) {

			FileDTO fileDTO = new FileDTO();
			fileDTO.setUrl(logo.getUrl());

			ImageType contentType = logo.getContentType();

			if (Validator.isNotNull(contentType)) {
				DataUri dataUri = new DataUri(contentType.getMediaType(),
						logo.getData());
				fileDTO.setData(dataUri.toString());
			}

			fileDTOs.add(fileDTO);
		}

		// an empty default logo
		if (fileDTOs.size() == 0) {
			fileDTOs.add(new FileDTO());
		}

		return fileDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.0.4
	 */
	public String getName() {

		String firstLast = getFullName(true);
		String lastFirst = getFullName(false);

		String name = lastFirst;

		if (Validator.isNull(firstLast)) {

			Organization organization = getVCard().getOrganization();

			if (organization != null) {

				List<String> values = organization.getValues();

				Iterator<String> iterator = values.iterator();

				StringBuilder sb = new StringBuilder();

				while (iterator.hasNext()) {

					sb.append(iterator.next());
					if (iterator.hasNext()) {
						sb.append(", ");
					}

				}

				name = sb.toString();

			}

		}

		return name;

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
			Iterator<String> iterator = names.iterator(); 
			
			while (iterator.hasNext()) {
				sb.append(iterator.next());
				if (iterator.hasNext()) {
					sb.append(", "); 
				}

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

		// an empty default note
		if (noteDTOs.size() == 0) {
			noteDTOs.add(new NoteDTO());
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
	 * @return the preferred phone.
	 * @since 1.0.8
	 */
	public PhoneDTO getPhone() {
		
		List<Telephone> phones = getVCard().getTelephoneNumbers(); 
		
		if (phones != null) {
			
			for (Telephone phone : phones) {
				Integer pref = phone.getPref(); 
				if (pref != null) {
					if (pref == 1) {
						return getPhone(phone); 
					}
				}				
			}			
		}

		Telephone phone = getVCard().getProperty(Telephone.class);

		return getPhone(phone);

	}

	/**
	 * 
	 * @param phone
	 * @return
	 * @since 1.0.8
	 */
	private PhoneDTO getPhone(Telephone phone) {

		PhoneDTO phoneDTO = new PhoneDTO();

		if (phone != null) {
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
		}

		return phoneDTO;
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

			PhoneDTO phoneDTO = getPhone(phone);

			phoneDTOs.add(phoneDTO);
		}

		// an empty default phone
		if (phoneDTOs.size() == 0) {
			phoneDTOs.add(new PhoneDTO());
		}

		return phoneDTOs;

	}

	/**
	 * 
	 * @return
	 * @since 1.1.2
	 */
	public List<FileDTO> getPhotos() {

		List<FileDTO> fileDTOs = new ArrayList<FileDTO>();

		List<Photo> photos = getVCard().getPhotos();

		for (Photo photo : photos) {

			FileDTO fileDTO = new FileDTO();
			fileDTO.setUrl(photo.getUrl());

			ImageType contentType = photo.getContentType();

			if (Validator.isNotNull(contentType)) {
				DataUri dataUri = new DataUri(contentType.getMediaType(),
						photo.getData());
				fileDTO.setData(dataUri.toString());
			}

			fileDTOs.add(fileDTO);
		}

		// an empty default photo
		if (fileDTOs.size() == 0) {
			fileDTOs.add(new FileDTO());
		}

		return fileDTOs;

	}

	/**
	 * 
	 * @return a dataURI for the entity the vCard represents, i.e. the first
	 *         photo if the vCard represents a person or a logo if the vCard
	 *         represents an organization.
	 * @since 1.1.6
	 */
	public String getPortrait() {

		String portrait = null;

		List<Photo> photos = getVCard().getPhotos();
		List<Logo> logos = getVCard().getLogos();

		if (logos.size() > 0) {
			portrait = getLogos().get(0).getData();
		} else if (photos.size() > 0) {
			portrait = getPhotos().get(0).getData();
		}

		return portrait;

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
	 * @since 1.1.3
	 */
	public List<FileDTO> getSounds() {

		List<FileDTO> fileDTOs = new ArrayList<FileDTO>();

		List<Sound> sounds = getVCard().getSounds();

		for (Sound sound : sounds) {

			FileDTO fileDTO = new FileDTO();
			fileDTO.setUrl(sound.getUrl());

			SoundType contentType = sound.getContentType();

			if (Validator.isNotNull(contentType)) {
				DataUri dataUri = new DataUri(contentType.getMediaType(),
						sound.getData());
				fileDTO.setData(dataUri.toString());
			}

			fileDTOs.add(fileDTO);
		}

		// an empty default sound
		if (fileDTOs.size() == 0) {
			fileDTOs.add(new FileDTO());
		}

		return fileDTOs;

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

			List<String> prefixes = sn.getPrefixes();
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

		// TODO: How do we handle multiple titles?
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

		// an empty default url
		if (urlDTOs.size() == 0) {
			urlDTOs.add(new UrlDTO());
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
