package com.neoris.santiago.controller;

import com.neoris.santiago.dto.ClienteDto;
import com.neoris.santiago.dto.PersonaDto;
import com.neoris.santiago.entity.ClienteEntity;
import com.neoris.santiago.entity.PersonaEntity;
import com.neoris.santiago.service.ClienteService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteService clienteService;

    @MockBean
    private ClienteService clienteMock;

    private PersonaDto personaDto;
    private ClienteDto clienteDto;
    private PersonaEntity personaEntity;
    private ClienteEntity clienteEntity;
    private String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("Admin:admin").getBytes()));

    @BeforeEach
    void init() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        personaDto = new PersonaDto();
        personaDto.setId(1);
        personaDto.setNombre("Pedro Picapiedra");
        personaDto.setIdentificacion("32187327");
        personaDto.setEdad(38);
        personaDto.setDireccion("Roca 3");
        personaDto.setGenero("MASCULINO");
        personaDto.setTelefono("3998847282");

        clienteDto = new ClienteDto();
        clienteDto.setId(1);
        clienteDto.setPersona(personaDto);
        clienteDto.setContrasena("1234");
        clienteDto.setEstado(true);

        personaEntity = new PersonaEntity();
        personaEntity.setId(1);
        personaEntity.setNombre("Coraje El Perro");
        personaEntity.setIdentificacion("103921093");
        personaEntity.setEdad(8);
        personaEntity.setDireccion("Suburbio r");
        personaEntity.setGenero("MASCULINO");
        personaEntity.setTelefono("3194932144");

        clienteEntity = new ClienteEntity();
        clienteEntity.setPersona(personaEntity);
        clienteEntity.setContrasena("1234");
        clienteEntity.setEstado(true);

    }

    @Test
    void validacionAutenticacionServiciosCorrecta() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isOk());
    }

    @Test
    void validacionErrorEnAutenticacion() throws Exception {
        String basicDigestHeaderValueFail = "Basic " + new String(Base64.encodeBase64(("fail:admin").getBytes()));
        mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
                        .header("Authorization", basicDigestHeaderValueFail).accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                )
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void creacionCorrectaDeCliente() {
        ClienteService cs = mock(ClienteService.class);
        doNothing().when(cs).crearCliente(clienteDto);
        cs.crearCliente(clienteDto);
        verify(cs, times(1)).crearCliente(clienteDto);
    }

    @Test
    void validacionErrorEnCreacionDeUsuario() {
        clienteDto.setEstado(null);
        ClienteService cs = mock(ClienteService.class);
        doNothing().when(cs).crearCliente(clienteDto);
        cs.crearCliente(clienteDto);
        verify(cs, times(1)).crearCliente(clienteDto);
    }


    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void getClienteByIdOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/1")
                        .header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void getClienteByIdError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/error")
                        .header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void validacionEdicionCorrectaCliente() throws Exception {
        clienteDto.setId(1);
        clienteDto.getPersona().setTelefono("3144431908");
        mockMvc.perform(MockMvcRequestBuilders.put("/clientes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clienteDto))
                )
                .andExpect(status().isOk());

        ArgumentCaptor<ClienteDto> captor = ArgumentCaptor.forClass(ClienteDto.class);
        Mockito.verify(clienteMock, Mockito.times(1)).editarCliente(captor.capture());
        assertEquals(captor.getValue().getId(), clienteDto.getId());
        assertEquals(captor.getValue().getPersona().getEdad(), clienteDto.getPersona().getEdad());
    }

    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void validacionEdicionClienteError() throws Exception {
        clienteDto.setPersona(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/clientes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clienteDto))
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void validarActualizacionDeCliente() throws Exception {
        clienteDto = null;
        mockMvc.perform(MockMvcRequestBuilders.put("/clientes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clienteDto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void eliminacionUsuarioErronea() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/12-03-2023")
                        .contentType("application/json")
                )
                .andExpect(status().isBadRequest());
    }

}
