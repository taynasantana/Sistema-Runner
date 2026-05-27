// tests/usecase/validate_usecase_test.go
package usecase_test

import (
	"os/exec"
	"testing"
)

func TestCasoDeUsoValidarAssinatura(t *testing.T) {

	cmd := exec.Command(
		"./assinatura",
		"validate",
		"--document", "contrato.pdf",
		"--signature", "abc123",
	)

	err := cmd.Run()

	if err != nil {
		t.Fatal("Falha no caso de uso de validação")
	}
}
