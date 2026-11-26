# Run AVENZO API with Cloudflare Tunnel

The named tunnel configuration is [cloudflare/config.yml](../cloudflare/config.yml). It sends `avenzo-api.bunli-it.site` to this Mac's Nginx gateway at `http://localhost:80`, where the API is served under `/api/v1`.

## 1. Check DNS first

```sh
dig NS bunli-it.site +short
```

The domain must return Cloudflare nameservers (for example, `clara.ns.cloudflare.com.` and `sean.ns.cloudflare.com.`). On 26 November 2026 it still returned Namecheap nameservers, so the DNS route has **not** been created. Finish the Cloudflare DNS migration and preserve existing DNS records before continuing.

## 2. Tunnel files and secrets

The existing `avenzo` tunnel is configured with:

```text

```

The credential JSON is outside the repository. `.gitignore` also excludes Cloudflare JSON, PEM, and token files; never copy credentials into Git.

## 3. Create the public hostname

After DNS uses Cloudflare nameservers, run this once:

```sh
cloudflared tunnel route dns avenzo avenzo-api.bunli-it.site
```

## 4. Start, stop, and restart

Start Docker Compose first:

```sh
docker compose up -d --build
curl -i http://localhost/api/v1/home
```

Start the named tunnel from the project root and keep the terminal open:

```sh
cloudflared tunnel --config cloudflare/config.yml run avenzo
```

Stop it with `Ctrl-C`. To restart, run the same start command again.

## 5. Validate

With the tunnel running:

```sh
cloudflared tunnel info avenzo
dig CNAME avenzo-api.bunli-it.site +short
curl -i https://avenzo-api.bunli-it.site/api/v1/home
```

The CNAME should point to `<tunnel-id>.cfargotunnel.com`, and the API should return a successful response. From Vercel, use:

```text
VITE_API_URL=https://avenzo-api.bunli-it.site/api/v1
```

The current API has no CORS configuration. Before connecting Vercel, allow only its exact origin, required methods, `Authorization`, `Content-Type`, and `OPTIONS`; do not use `*` for production.

Media URLs still default to `http://localhost:9000`. A Vercel visitor needs a separate public media hostname configured as `MINIO_PUBLIC_ENDPOINT`; do not expose the MinIO console.

## Troubleshooting and security

- **502:** run `docker compose logs -f nginx api` and confirm `curl -i http://localhost/` works.
- **Tunnel cannot connect:** update `cloudflared` and permit outbound Cloudflare Tunnel traffic (commonly port 7844).
- **Mac unavailable:** keep Docker Desktop and `cloudflared` running; use `caffeinate -dims` during a demo.

The public hostname is internet-accessible. Use strong secrets, restrict CORS, keep credentials private, and use Cloudflare Access/WAF before real customer or payment traffic.
