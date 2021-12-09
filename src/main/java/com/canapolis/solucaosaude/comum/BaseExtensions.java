package com.canapolis.solucaosaude.comum;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseExtensions {

    @Autowired
    private ModelMapper modelMapper;

//    @Autowired
//    protected PasswordEncoder passwordEncoder;
//
//    protected String passwordEncoder(String rawPassword) {
//        return this.passwordEncoder.encode(rawPassword);
//    }

    public <D> D convertToModel(Object dto, Class<D> model) { return this.convertTo(dto, model); }

    public <D> D convertToDTO(Object model, Class<D> dto) { return this.convertTo(model, dto); }

    public <D> D convertTo(Object source, Class<D> destinationType) { return this.modelMapper.map(source, destinationType); }

    public <S, D> List<D> convertListTo(List<S> source, Class<D> destinationType) {
        return source.stream().map(element -> this.convertTo(element, destinationType)).collect(Collectors.toList()); }

//    protected String getCurrentUser() { return UserContextUtil.getCurrentUser(); }
//
//    protected UUID toUUID(String id) { return UUID.fromString(id); }
//
//    protected UUID getUUID() { return UUID.randomUUID(); }

    /**
     * Monta location para o recurso
     *
     * @param id Indentificador do recurso.
     * @return HttpHeaders
     */
//    protected HttpHeaders getHttpHeaders(Object id) {
//        URI location;
//
//        if (id == null) {
//            location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
//        } else {
//            location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
//        }
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set(HttpHeaders.LOCATION, location.toString());
//        return responseHeaders;
//    }
//    protected HttpHeaders getHttpHeaders(String path, Object... uriVariableValues) {
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(path).buildAndExpand(uriVariableValues).toUri();
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        responseHeaders.set(HttpHeaders.LOCATION, location.toString());
//
//        return responseHeaders;
//    }
//    protected String constructBaseUrl(HttpServletRequest request) {
//        String scheme = request.getScheme();
//        if (request.getHeader("x-forwarded-proto") != null) {
//            scheme = request.getHeader("x-forwarded-proto");
//        }
//        int serverPort = request.getServerPort();
//        if (request.getHeader("x-forwarded-port") != null) {
//            try {
//                serverPort = request.getIntHeader("x-forwarded-port");
//            } catch (NumberFormatException e) {
//            }
//        }
//        String baseUrl = String.format("%s://%s:%d", scheme, request.getServerName(), serverPort);
//        return baseUrl;
//    }
//
//    protected String constructBaseUrl(String format, Object... args) {
//        UriComponents uc    = ServletUriComponentsBuilder.fromCurrentRequest().build();
//        String scheme       = uc.getScheme();
//        int port            = uc.getPort();
//        String host         = uc.getHost();
//        String baseUrl      = String.format("%s://%s:%d", scheme, host, port);
//        return (StringUtils.isNotEmpty(format) && ObjectUtils.isNotEmpty(args) ? baseUrl.format(baseUrl + format, args) : baseUrl);
//    }
//    protected URI newURI(String url) throws DefaultExceptionHandler {
//        try {
//            return new URI(url);
//        } catch (URISyntaxException e) {
//            throw new DefaultExceptionHandler(e);
//        }
//    }
}
