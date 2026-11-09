package kh.edu.istad.platform.iam.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEditForm {

    private String givenName;
    private String familyName;
    private String middleName;
    private String nickname;
    private String gender;
    private String birthdate;
    private String phoneNumber;
    private String zoneinfo;
    private String locale;
    private String streetAddress;
    private String locality;
    private String region;
    private String postalCode;
    private String country;
}
