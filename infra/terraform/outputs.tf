output "instance_ip" {
  description = "Dirección IP pública de la instancia"
  value       = google_compute_instance.iot_api_vm.network_interface[0].access_config[0].nat_ip
}
