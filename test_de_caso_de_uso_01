// tests/usecase/sign_usecase_test.go
package usecase_test

import (
	"os/exec"
	"testing"
)

func TestCasoDeUsoCriarAssinatura(t *testing.T) {

	cmd := exec.Command(
		"./assinatura",
		"sign",
		"--document", "contrato.pdf",
		"--signer", "joao",
	)

	err := cmd.Run()

	if err != nil {
		t.Fatal("Falha no caso de uso de criação de assinatura")
	}
}
