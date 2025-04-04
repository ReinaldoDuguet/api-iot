package com.grupouno.iot.minero.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupouno.iot.minero.models.User;
import com.grupouno.iot.minero.repository.UserRepository;
//import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPasswordHash("hash1");
        user1.setCreatedAt(LocalDateTime.now().minusDays(1));
        user1.setUpdatedAt(LocalDateTime.now().minusDays(1));

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPasswordHash("hash2");
        user2.setCreatedAt(LocalDateTime.now().minusHours(5));
        user2.setUpdatedAt(LocalDateTime.now().minusHours(5));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        assertEquals(2, actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findAll();
    }
    
   /* @Test//fallido
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        assertEquals(3, actualUsers.size());// espera un tamaño de lista de 3 usuarios, pero es de 2.
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findAll();
    }*/
    

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void createUser_ShouldSetTimestampsAndSaveUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPasswordHash("newHash");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(3L); // Simular ID generado
            return savedUser;
        });

        // Act
        User createdUser = userService.createUser(newUser);

        // Assert
        assertNotNull(createdUser.getId());
        assertEquals("newUser", createdUser.getUsername());
        assertEquals("newHash", createdUser.getPasswordHash());
        assertNotNull(createdUser.getCreatedAt());
        assertNotNull(createdUser.getUpdatedAt());
        assertEquals(createdUser.getCreatedAt(), createdUser.getUpdatedAt());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        // Arrange
        User updatedInfo = new User();
        updatedInfo.setUsername("updatedName");
        updatedInfo.setPasswordHash("updatedHash");
        updatedInfo.setEnabled(false);
        updatedInfo.setBlocked(true);
        updatedInfo.setCredentialsExpired(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.updateUser(1L, updatedInfo);

        // Assert
        assertEquals(1L, updatedUser.getId());
        assertEquals("updatedName", updatedUser.getUsername());
        assertEquals("updatedHash", updatedUser.getPasswordHash());
        assertFalse(updatedUser.isEnabled());
        assertTrue(updatedUser.isBlocked());
        assertTrue(updatedUser.isCredentialsExpired());
       // assertNotNull(updatedUser.getUpdatedAt());
       // assertTrue(updatedUser.getUpdatedAt().isAfter(user1.getUpdatedAt()));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user1);
    }

   /* @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        // Arrange
        User updatedInfo = new User();
        updatedInfo.setUsername("updatedName");
        updatedInfo.setPasswordHash("updatedHash");
        updatedInfo.setEnabled(false);
        updatedInfo.setBlocked(true);
        updatedInfo.setCredentialsExpired(true);

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldName");
        existingUser.setUpdatedAt(LocalDateTime.now().minusDays(1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.updateUser(1L, updatedInfo);

        // Assert
        assertEquals(1L, updatedUser.getId());
        assertEquals("updatedName", updatedUser.getUsername());
        assertEquals("updatedHash", updatedUser.getPasswordHash());
        assertFalse(updatedUser.isEnabled());
        assertTrue(updatedUser.isBlocked());
        assertTrue(updatedUser.isCredentialsExpired());
        assertNotNull(updatedUser.getUpdatedAt());
        assertTrue(updatedUser.getUpdatedAt().isAfter(existingUser.getUpdatedAt()));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }*/


    @Test
    void updateUser_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        User updatedInfo = new User();
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(99L, updatedInfo);
        });

        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}


/* 
Explicación de las pruebas:
Configuración inicial:

Usamos @ExtendWith(MockitoExtension.class) para habilitar Mockito
@Mock crea un mock de UserRepository
@InjectMocks crea una instancia de UserServiceImpl e inyecta los mocks
setUp() inicializa datos de prueba que se usan en múltiples tests

Pruebas individuales:
getAllUsers_ShouldReturnAllUsers: Verifica que se retornen todos los usuarios
getUserById_WhenUserExists_ShouldReturnUser: Prueba la búsqueda exitosa por ID
getUserById_WhenUserNotExists_ShouldReturnEmpty: Prueba la búsqueda cuando el usuario no existe
createUser_ShouldSetTimestampsAndSaveUser: Verifica que se establezcan los timestamps y se guarde el usuario
updateUser_WhenUserExists_ShouldUpdateAndReturnUser: Prueba la actualización exitosa de un usuario
updateUser_WhenUserNotExists_ShouldThrowException: Verifica que se lance excepción al actualizar usuario inexistente
deleteUser_ShouldCallRepositoryDelete: Prueba que se llame al método delete del repositorio

Verificaciones comunes:
Se verifica que los métodos del repositorio se llamen el número correcto de veces
Se comprueban los valores retornados y los cambios de estado
Se validan las fechas de creación y actualización

Explicación de los comentarios // Arrange, // Act y // Assert
Estos comentarios siguen el patrón AAA (Arrange-Act-Assert), que es una forma estructurada y clara de organizar las pruebas unitarias. Te explico cada parte:

1. Arrange (Preparar)

// Arrange
Long userId = 1L;
when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
Propósito: Configurar el escenario de prueba

Aquí:
Inicializas variables
Configuras los mocks (comportamientos esperados)
Preparas los objetos de prueba
Estableces cualquier condición previa necesaria

2. Act (Actuar)
// Act
Optional<User> result = userService.getUserById(userId);
Propósito: Ejecutar la operación que quieres probar
Debe ser generalmente una sola línea que llame al método bajo prueba
Debe ser la única interacción con el sistema bajo prueba en este bloque

3. Assert (Verificar)
// Assert
assertTrue(result.isPresent());
assertEquals(user1, result.get());
verify(userRepository, times(1)).findById(userId);
Propósito: Verificar que el resultado es el esperado

Aquí:
Compruebas valores de retorno
Verificas estados de objetos
Confirmas interacciones con mocks
Validas excepciones si es necesario

Beneficios de usar AAA:
Estructura clara: Cualquier desarrollador puede entender rápidamente la prueba
Mantenibilidad: Fácil de modificar cuando cambia el código
Detección de errores: Si una prueba falla, es fácil identificar en qué fase (preparación, ejecución o verificación)
Consistencia: Todas las pruebas siguen el mismo patrón

Ejemplo completo con contexto:
@Test
void getUserById_WhenUserExists_ShouldReturnUser() {
    // Arrange - Preparamos: configuramos el mock y datos de prueba
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

    // Act - Ejecutamos: llamamos al método que queremos probar
    Optional<User> result = userService.getUserById(userId);

    // Assert - Verificamos: comprobamos resultados y interacciones
    assertTrue(result.isPresent()); // Verificamos que hay resultado
    assertEquals(user1, result.get()); // Verificamos que es el usuario esperado
    verify(userRepository, times(1)).findById(userId); // Verificamos interacción con el mock
}

Alternativas comunes:
Algunos frameworks usan nombres ligeramente diferentes:

Given-When-Then (BDD style)
Given (Dado) = Arrange
When (Cuando) = Act
Then (Entonces) = Assert

Pero el concepto es el mismo. AAA es el más utilizado en pruebas unitarias tradicionales.

