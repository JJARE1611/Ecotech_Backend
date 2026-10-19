package com.upc.appecotech.servicios;

import com.upc.appecotech.dtos.ProductoDTO;
import com.upc.appecotech.entidades.Producto;
import com.upc.appecotech.interfaces.IProductoService;
import com.upc.appecotech.repositorios.ProductoRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductoService implements IProductoService {
    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("activo", "agotado", "inactivo");
    private static final List<String> CATEGORIAS_VALIDAS = Arrays.asList("Accesorios", "Hogar", "Tecnología", "Sostenible");

    @Override
    @Transactional
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = modelMapper.map(productoDTO, Producto.class);
        producto.setFecha(LocalDate.now());

        if (producto.getEstado() == null || producto.getEstado().isEmpty()) {
            producto.setEstado("activo");
        }

        if (!ESTADOS_VALIDOS.contains(producto.getEstado().toLowerCase())) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: activo, agotado o inactivo");
        }

        if (producto.getCategoria() != null && !producto.getCategoria().isEmpty()) {
            if (!CATEGORIAS_VALIDAS.contains(producto.getCategoria())) {
                throw new IllegalArgumentException("Categoría inválida. Debe ser: Accesorios, Hogar, Tecnología o Sostenible");
            }
        }

        if (producto.getStock() == null) {
            producto.setStock(0);
        }

        if (producto.getStock() == 0 && !"inactivo".equals(producto.getEstado())) {
            producto.setEstado("agotado");
        }

        // 🆕 ESTABLECER VALOR POR DEFECTO PARA ENTREGA DIGITAL
        if (producto.getPermiteEntregaDigital() == null) {
            producto.setPermiteEntregaDigital(false);
        }

        Producto guardado = productoRepositorio.save(producto);
        return modelMapper.map(guardado, ProductoDTO.class);
    }

    @Override
    @Transactional
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        return productoRepositorio.findById(id)
                .map(productoExistente -> {
                    if (productoDTO.getNombre() != null) {
                        productoExistente.setNombre(productoDTO.getNombre());
                    }
                    if (productoDTO.getDescripcion() != null) {
                        productoExistente.setDescripcion(productoDTO.getDescripcion());
                    }
                    if (productoDTO.getPuntosrequerido() != null) {
                        productoExistente.setPuntosrequerido(productoDTO.getPuntosrequerido());
                    }

                    // Actualizar stock
                    if (productoDTO.getStock() != null) {
                        productoExistente.setStock(productoDTO.getStock());
                        // Actualizar estado automáticamente según el stock
                        if (productoDTO.getStock() == 0 && !"inactivo".equals(productoExistente.getEstado())) {
                            productoExistente.setEstado("agotado");
                        } else if (productoDTO.getStock() > 0 && "agotado".equals(productoExistente.getEstado())) {
                            productoExistente.setEstado("activo");
                        }
                    }

                    if (productoDTO.getPrecioReferencia() != null) {
                        productoExistente.setPrecioReferencia(productoDTO.getPrecioReferencia());
                    }

                    if (productoDTO.getCategoria() != null) {
                        if (!productoDTO.getCategoria().isEmpty() && !CATEGORIAS_VALIDAS.contains(productoDTO.getCategoria())) {
                            throw new IllegalArgumentException("Categoría inválida. Debe ser: Accesorios, Hogar, Tecnología o Sostenible");
                        }
                        productoExistente.setCategoria(productoDTO.getCategoria());
                    }

                    if (productoDTO.getImagen() != null) {
                        productoExistente.setImagen(productoDTO.getImagen());
                    }

                    if (productoDTO.getEstado() != null) {
                        if (!ESTADOS_VALIDOS.contains(productoDTO.getEstado().toLowerCase())) {
                            throw new IllegalArgumentException("Estado inválido. Debe ser: activo, agotado o inactivo");
                        }
                        productoExistente.setEstado(productoDTO.getEstado());
                    }

                    // 🆕 ACTUALIZAR CAMPO PERMITE ENTREGA DIGITAL
                    if (productoDTO.getPermiteEntregaDigital() != null) {
                        productoExistente.setPermiteEntregaDigital(productoDTO.getPermiteEntregaDigital());
                    }

                    Producto actualizado = productoRepositorio.save(productoExistente);
                    return modelMapper.map(actualizado, ProductoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public ProductoDTO buscarPorId(Long id) {
        return productoRepositorio.findById(id)
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarTodos() {
        List<Producto> productos = productoRepositorio.findAll();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorPuntosAscendente() {
        List<Producto> productos = productoRepositorio.findAllByOrderByPuntosrequeridoAsc();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorPuntosDescendente() {
        List<Producto> productos = productoRepositorio.findAllByOrderByPuntosrequeridoDesc();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorRangoPuntos(Integer min, Integer max) {
        List<Producto> productos = productoRepositorio.findByPuntosrequeridoBetween(min, max);
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepositorio.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepositorio.deleteById(id);
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorCategoria(String categoria) {
        if (!CATEGORIAS_VALIDAS.contains(categoria)) {
            throw new IllegalArgumentException("Categoría inválida. Debe ser: Accesorios, Hogar, Tecnología o Sostenible");
        }
        List<Producto> productos = productoRepositorio.findByCategoria(categoria);
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado.toLowerCase())) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: activo, agotado o inactivo");
        }
        List<Producto> productos = productoRepositorio.findByEstado(estado);
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarProductosActivos() {
        List<Producto> productos = productoRepositorio.findProductosActivos();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarProductosDisponibles() {
        List<Producto> productos = productoRepositorio.findProductosDisponibles();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarPorCategoriaYEstado(String categoria, String estado) {
        if (!ESTADOS_VALIDOS.contains(estado.toLowerCase())) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: activo, agotado o inactivo");
        }
        if (!CATEGORIAS_VALIDAS.contains(categoria)) {
            throw new IllegalArgumentException("Categoría inválida. Debe ser: Accesorios, Hogar, Tecnología o Sostenible");
        }
        List<Producto> productos = productoRepositorio.findByCategoriaAndEstado(categoria, estado);
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<ProductoDTO> listarProductosActivosPorCategoria(String categoria) {
        if (!CATEGORIAS_VALIDAS.contains(categoria)) {
            throw new IllegalArgumentException("Categoría inválida. Debe ser: Accesorios, Hogar, Tecnología o Sostenible");
        }
        List<Producto> productos = productoRepositorio.findProductosActivosPorCategoria(categoria);
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public List<String> listarCategorias() {
        return CATEGORIAS_VALIDAS;
    }

    @Override
    @Transactional
    public ProductoDTO actualizarStock(Long id, Integer nuevoStock) {
        return productoRepositorio.findById(id)
                .map(producto -> {
                    producto.setStock(nuevoStock);

                    // Actualizar estado automáticamente según el stock
                    if (nuevoStock == 0 && !"inactivo".equals(producto.getEstado())) {
                        producto.setEstado("agotado");
                    } else if (nuevoStock > 0 && "agotado".equals(producto.getEstado())) {
                        producto.setEstado("activo");
                    }

                    Producto actualizado = productoRepositorio.save(producto);
                    return modelMapper.map(actualizado, ProductoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public ProductoDTO cambiarEstado(Long id, String nuevoEstado) {
        if (!ESTADOS_VALIDOS.contains(nuevoEstado.toLowerCase())) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: activo, agotado o inactivo");
        }

        return productoRepositorio.findById(id)
                .map(producto -> {
                    producto.setEstado(nuevoEstado);
                    Producto actualizado = productoRepositorio.save(producto);
                    return modelMapper.map(actualizado, ProductoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    // 🆕 NUEVO MÉTODO: Actualizar solo el campo permite entrega digital
    @Transactional
    public ProductoDTO actualizarPermiteEntregaDigital(Long id, Boolean permiteEntregaDigital) {
        return productoRepositorio.findById(id)
                .map(producto -> {
                    producto.setPermiteEntregaDigital(permiteEntregaDigital);
                    Producto actualizado = productoRepositorio.save(producto);
                    return modelMapper.map(actualizado, ProductoDTO.class);
                })
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    // 🆕 NUEVO MÉTODO: Listar productos que permiten entrega digital
    @Transactional
    public List<ProductoDTO> listarProductosDigitales() {
        List<Producto> productos = productoRepositorio.findByPermiteEntregaDigital(true);
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .toList();
    }
}