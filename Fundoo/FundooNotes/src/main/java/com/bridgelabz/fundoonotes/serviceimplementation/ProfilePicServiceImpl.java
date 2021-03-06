package com.bridgelabz.fundoonotes.serviceimplementation;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.bridgelabz.fundoonotes.model.ProfilePic;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.ProfilePicRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.ProfilePicService;
import com.bridgelabz.fundoonotes.utility.Jwt;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfilePicServiceImpl implements ProfilePicService {
	
	@Autowired
	private ProfilePicRepository profilePicRepository;

	@Autowired
	private Jwt jwtGenerator;

	@Autowired
	private UserRepository userRepository;

	@Value("${aws.bucket.name}")
	private String bucketName;

	@Autowired
	private AmazonS3 amazonS3Client;

	@Override
	public ProfilePic storeObjectInS3(MultipartFile file, String fileName, String contentType, String token){
		try {
			System.out.println(contentType+" "+fileName);
			long userId = jwtGenerator.parseJwtToken(token);
			UserModel user = userRepository.findbyId(userId);
			if (user != null) {
				ProfilePic profile = new ProfilePic(fileName, user);
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(contentType);
				objectMetadata.setContentLength(file.getSize());
				System.out.println("1");
				System.out.println(bucketName+" "+fileName+" "+file.getInputStream()+" "+objectMetadata);
				amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);
				System.out.println("11");
				profilePicRepository.save(profile);
				return profile;
			}
		} catch (AmazonClientException | IOException exception) {
			throw new RuntimeException("Error while uploading file.");
		}
		return null;
	}

	@Override
	public ProfilePic updateProfilePic(MultipartFile file, String originalFile,String contentType,String token) {
		try {
			long userId=jwtGenerator.parseJwtToken(token);
			UserModel user=userRepository.findbyId(userId);
			System.out.println(user.getEmail());
			ProfilePic profile=profilePicRepository.findByUserId(userId);
			System.out.println(profile.getProfilePicName());
			
			if(user!=null&& profile!=null) {
				
				deleteProfilePic(profile.getProfilePicName());
				profilePicRepository.delete(profile);
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(contentType);
				objectMetadata.setContentLength(file.getSize());
				amazonS3Client.putObject(bucketName,originalFile,file.getInputStream(),objectMetadata);
				profilePicRepository.save(profile);
				return profile;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteProfilePic(String key) {
		try {
			amazonS3Client.deleteObject(bucketName, key);
		} catch (AmazonServiceException serviceException) {
			log.error(serviceException.getErrorMessage());
		} catch (AmazonClientException exception) {
			log.error("Something went wrong while deleting File.");
		}
	}

	@Override
	public S3Object getProfilePic(MultipartFile file, String token) {
		try {
			long userId = jwtGenerator.parseJwtToken(token);
			Optional<UserModel> user = userRepository.findById(userId);
			if (user != null) {
				ProfilePic profile = profilePicRepository.findByUserId(userId);
				if (profile != null) {
					return fetchObject(profile.getProfilePicName());
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public S3Object fetchObject(String awsFileName) {
		S3Object s3Object;
		try {
			s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, awsFileName));
		} catch (AmazonServiceException serviceException) {


			throw new RuntimeException("Error while streaming File.");
		} catch (AmazonClientException exception) {

			throw new RuntimeException("Error while streaming File.");
		}
		return s3Object;
	}

}
