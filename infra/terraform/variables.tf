variable "credentials_file" {
  description = "Ruta al archivo de credenciales JSON"
  type        = string
  default     = "C:/Users/Julio/.gcp/github-deployer.json"
  # <-- cámbialo con tu path real
}

variable "project_id" {
  description = "ID del proyecto GCP"
  type        = string
  default     = "iot-minero-api-prod"
}

variable "region" {
  description = "Región de GCP"
  type        = string
  default     = "southamerica-west1"
}

variable "zone" {
  description = "Zona dentro de la región"
  type        = string
  default     = "southamerica-west1-a"
}
