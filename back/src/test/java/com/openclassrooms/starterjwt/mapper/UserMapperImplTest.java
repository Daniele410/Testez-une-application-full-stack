package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {
    @InjectMocks
    private UserMapperImpl userMapper = new UserMapperImpl();

    @Test
    void testToDto() {
        // given
        User user = new User();
        user.setId(1L);
        user.setFirstName("Mario");
        user.setLastName("Rossi");
        user.setEmail("mail@mail.com");

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        // when
        UserDto userDto1 = userMapper.toDto(user);

        // then
        assertEquals(user.getId(), userDto1.getId());
    }

    @Test
    void toEntity_WithValidDto_ReturnsCorrectEntity() {
        // given
        UserMapperImpl userMapper = new UserMapperImpl();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPassword("password");

        // when
        User user = userMapper.toEntity(userDto);

        // then
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
    }

    @Test
    void toDto_WithValidEntity_ReturnsCorrectDto() {
        // given
        UserMapperImpl userMapper = new UserMapperImpl();
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .build();

        // when
        UserDto userDto = userMapper.toDto(user);

        // then
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());

    }

    @Test
    void toEntityList_WithValidDtoList_ReturnsCorrectEntityList() {
        // given
        UserMapperImpl userMapper = new UserMapperImpl();
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("test1@example.com");
        userDto1.setLastName("Doe");
        userDto1.setFirstName("John");
        userDto1.setPassword("password");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("test2@example.com");
        userDto2.setLastName("Smith");
        userDto2.setFirstName("Jane");
        userDto2.setPassword("password");

        List<UserDto> dtoList = Arrays.asList(userDto1, userDto2);

        // when
        List<User> userList = userMapper.toEntity(dtoList);

        // then
        assertEquals(dtoList.size(), userList.size());
    }

    @Test
    void toDtoList_WithValidEntityList_ReturnsCorrectDtoList() {
        // given
        UserMapperImpl userMapper = new UserMapperImpl();
        User user1 = User.builder()
                .id(1L)
                .email("test1@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password")
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("test2@example.com")
                .lastName("Smith")
                .firstName("Jane")
                .password("password")
                .build();

        List<User> entityList = Arrays.asList(user1, user2);

        // when
        List<UserDto> dtoList = userMapper.toDto(entityList);

        // then
        assertEquals(entityList.size(), dtoList.size());
    }

    @Test
    void toDtoList_WithNullEntityList_ReturnsNull() {
        // given // when
        List<UserDto> dtoList = userMapper.toDto((List<User>) null);

        // then
        assertNull(dtoList);
    }

    @Test
    void toEntityList_WithNullDtoList_ReturnsNull() {
        // given // when
        List<User> entityList = userMapper.toEntity((List<UserDto>) null);

        // then
        assertNull(entityList);
    }

    @Test
    void userToEntity_WithNullDto_ReturnsNull() {

        //given //when
        User result = userMapper.toEntity((UserDto) null);

        // then
        assertNull(result);
    }

    @Test
    void toDto_WithNullEntity_ReturnsNull() {
        // given // when
        UserDto result = userMapper.toDto((User) null);

        // then
        assertNull(result);
    }
}
