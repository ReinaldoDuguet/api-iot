resource "google_compute_instance" "iot_vm" {
  name         = var.vm_name
  machine_type = var.machine_type
  zone         = var.zone
  project      = var.project_id

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
      size  = 30
    }
  }

  metadata_startup_script = file("${path.module}/../../startup.sh")

  network_interface {
    network = "default"
    access_config {}
  }
}
