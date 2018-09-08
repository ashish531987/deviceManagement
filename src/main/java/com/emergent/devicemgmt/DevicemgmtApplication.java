package com.emergent.devicemgmt;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.domain.Role;
import com.emergent.devicemgmt.domain.User;
import com.emergent.devicemgmt.repository.DeviceJpaRepository;
import com.emergent.devicemgmt.repository.RoleJpaRepository;
import com.emergent.devicemgmt.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DevicemgmtApplication {
/*
	This commented code is to populate test data while testing with actual database server.
	@Autowired
	UserJpaRepository userJpaRepository;

	@Autowired
	RoleJpaRepository roleJpaRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	DeviceJpaRepository deviceJpaRepository;
*/

	public static void main(String[] args) {
		SpringApplication.run(DevicemgmtApplication.class, args);
	}
/*
	@PostConstruct
	private void populateDB(){
		// Role Admin
		Role adminRole = new Role();
		adminRole.setRole("ADMIN");
		// Role User
		Role userRole = new Role();
		userRole.setRole("USER");
		// Role Guest
		Role guestRole = new Role();
		guestRole.setRole("GUEST");

		adminRole = roleJpaRepository.save(adminRole);
		userRole = roleJpaRepository.save(userRole);
		guestRole = roleJpaRepository.save(guestRole);

		// User 1 - Admin, User
		User user1 = new User();
		user1.setEmail("ashish@anoosmar.com");
		user1.setFullName("Ashish Mahamuni");
		user1.setPassword(passwordEncoder.encode("pass@123"));

		user1 = userJpaRepository.save(user1);

		user1.addRole(adminRole);
//		user1.addRole(userRole);
		userJpaRepository.save(user1);

		// User 2 - User
		User user2 = new User();
		user2.setEmail("mangesh@vaultize.com");
		user2.setFullName("Mangesh Kshirsagar");
		user2.setPassword(passwordEncoder.encode("vault@123"));

		user2 = userJpaRepository.save(user2);

		user2.addRole(userRole);
		userJpaRepository.save(user2);

		// User 3 - Guest
		User user3 = new User();
		user3.setEmail("mayur@vaultize.com");
		user3.setFullName("Mayur Kore");
		user3.setPassword(passwordEncoder.encode("vault@123"));

		user3 = userJpaRepository.save(user3);
		user3.addRole(guestRole);

		userJpaRepository.save(user3);

		Device device = new Device();
		device.setDeviceName("Android Phone");
		device.setUniqueId("123456789");
		deviceJpaRepository.save(device);

		Device device1 = new Device();
		device1.setDeviceName("IPhone 6");
		device1.setUniqueId("1234456767");
		deviceJpaRepository.save(device1);
	}
*/
}
