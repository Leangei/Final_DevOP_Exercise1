# PowerShell script to run Ansible playbook using Docker
# Usage: .\run-ansible.ps1

$IMAGE = "alpine/ansible:latest"
$PLAYBOOK = "deploy.yml"
$INVENTORY = "hosts.ini"

Write-Host "=== Pulling Ansible Docker image..." -ForegroundColor Cyan
docker pull $IMAGE

Write-Host "=== Running Ansible Playbook..." -ForegroundColor Cyan
Write-Host "Target: CHOICE_A (192.168.1.10)" -ForegroundColor Yellow
Write-Host ""

$SSH_DIR = "$HOME\.ssh"

docker run --rm -it `
    -v "${PWD}:/ansible:ro" `
    -v "${SSH_DIR}:/root/.ssh:ro" `
    -w /ansible `
    $IMAGE `
    ansible-playbook -i /ansible/$INVENTORY /ansible/$PLAYBOOK -v

if ($LASTEXITCODE -eq 0) {
    Write-Host "=== Playbook executed successfully!" -ForegroundColor Green
} else {
    Write-Host "=== Playbook execution failed with exit code: $LASTEXITCODE" -ForegroundColor Red
}