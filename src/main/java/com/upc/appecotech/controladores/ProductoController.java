package com.upc.appecotech.controladores;

import com.upc.appecotech.dtos.ProductoDTO;
import com.upc.appecotech.interfaces.IProductoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ProductoController {
    @Autowired
    private IProductoService productoService;



    @PostMapping("/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearProducto(@RequestBody ProductoDTO productoDTO) {
        try {
            ProductoDTO nuevoProducto = productoService.crearProducto(productoDTO);
            return ResponseEntity.ok(nuevoProducto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        try {
            ProductoDTO actualizado = productoService.actualizarProducto(id, productoDTO);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.buscarPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Producto no encontrado");
    }

    @DeleteMapping("/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/productos/ordenar/puntos-asc")
    public ResponseEntity<List<ProductoDTO>> listarPorPuntosAscendente() {
        return ResponseEntity.ok(productoService.listarPorPuntosAscendente());
    }

    @GetMapping("/productos/ordenar/puntos-desc")
    public ResponseEntity<List<ProductoDTO>> listarPorPuntosDescendente() {
        return ResponseEntity.ok(productoService.listarPorPuntosDescendente());
    }

    @GetMapping("/productos/rango")
    public ResponseEntity<List<ProductoDTO>> listarPorRango(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        return ResponseEntity.ok(productoService.listarPorRangoPuntos(min, max));
    }


    @GetMapping("/productos/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        return ResponseEntity.ok(productoService.listarCategorias());
    }

    @GetMapping("/productos/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
    }

    @GetMapping("/productos/categoria/{categoria}/activos")
    public ResponseEntity<List<ProductoDTO>> listarProductosActivosPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarProductosActivosPorCategoria(categoria));
    }

    @GetMapping("/productos/estado/{estado}")
    public ResponseEntity<?> listarPorEstado(@PathVariable String estado) {
        try {
            return ResponseEntity.ok(productoService.listarPorEstado(estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/productos/activos")
    public ResponseEntity<List<ProductoDTO>> listarProductosActivos() {
        return ResponseEntity.ok(productoService.listarProductosActivos());
    }

    @GetMapping("/productos/disponibles")
    public ResponseEntity<List<ProductoDTO>> listarProductosDisponibles() {
        return ResponseEntity.ok(productoService.listarProductosDisponibles());
    }

    @PutMapping("/productos/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estado");
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'estado' es requerido");
            }
            ProductoDTO actualizado = productoService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/productos/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        try {
            Integer nuevoStock = body.get("stock");
            if (nuevoStock == null) {
                return ResponseEntity.badRequest().body("El campo 'stock' es requerido");
            }
            if (nuevoStock < 0) {
                return ResponseEntity.badRequest().body("El stock no puede ser negativo");
            }
            ProductoDTO actualizado = productoService.actualizarStock(id, nuevoStock);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/productos/filtrar")
    public ResponseEntity<?> filtrarProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String estado) {
        try {
            if (categoria != null && estado != null) {
                return ResponseEntity.ok(productoService.listarPorCategoriaYEstado(categoria, estado));
            } else if (categoria != null) {
                return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
            } else if (estado != null) {
                return ResponseEntity.ok(productoService.listarPorEstado(estado));
            } else {
                return ResponseEntity.ok(productoService.listarTodos());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/productos/{id}/permite-entrega-digital")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarPermiteEntregaDigital(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        try {
            Boolean permiteEntregaDigital = body.get("permiteEntregaDigital");
            if (permiteEntregaDigital == null) {
                return ResponseEntity.badRequest().body("El campo 'permiteEntregaDigital' es requerido");
            }
            ProductoDTO actualizado = productoService.actualizarPermiteEntregaDigital(id, permiteEntregaDigital);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar productos digitales
    @GetMapping("/productos/digitales")
    public ResponseEntity<List<ProductoDTO>> listarProductosDigitales() {
        return ResponseEntity.ok(productoService.listarProductosDigitales());
    }
}
