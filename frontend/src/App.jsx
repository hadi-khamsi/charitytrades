import { useState, useEffect } from 'react'
import './App.css'

const API = '/api'

const LIFECYCLE_INFO = {
  NEW: {
    title: 'Order Received',
    traditional: 'Buy order submitted to your broker',
    here: 'Donation order received by CharityTrades'
  },
  VALIDATED: {
    title: 'Pre-Trade Risk Check',
    traditional: 'Broker checks if you have funds/margin',
    here: 'Validates donation meets minimum amount'
  },
  ROUTED: {
    title: 'Order Routing',
    traditional: 'Broker routes to NYSE, NASDAQ, or dark pool based on best execution',
    here: 'Smart order router checks internal CLOB for corporate matchers. If liquidity exists, routes internally; otherwise routes DIRECT to GlobalGiving (external venue)'
  },
  MATCHED: {
    title: 'Matched on Book',
    traditional: 'Your bid matched with a seller\'s ask on the order book',
    here: 'Paired with a corporate matching pledge on internal CLOB'
  },
  EXECUTED: {
    title: 'Direct Execution',
    traditional: 'Market order executed at best available price, no resting order match',
    here: 'No internal liquidity—routed directly to GlobalGiving as external execution venue'
  },
  CLEARING: {
    title: 'Clearing',
    traditional: 'Clearinghouse verifies both sides can settle',
    here: 'User completing payment on GlobalGiving'
  },
  SETTLED: {
    title: 'Settlement',
    traditional: 'T+2: Securities and cash exchanged',
    here: 'Donation confirmed and recorded'
  }
}

// World Giving Index data (Source: CAF World Giving Index 2010-2023)
// Measures global participation in giving behaviors (helping stranger, donating money, volunteering)
const GIVING_INDEX_DATA = [
  { year: 2010, index: 31.0 },
  { year: 2011, index: 32.0 },
  { year: 2012, index: 31.5 },
  { year: 2013, index: 33.0 },
  { year: 2014, index: 31.5 },
  { year: 2015, index: 31.8 },
  { year: 2016, index: 32.5 },
  { year: 2017, index: 33.8 },
  { year: 2018, index: 34.0 },
  { year: 2019, index: 34.2 },
  { year: 2020, index: 35.5 },
  { year: 2021, index: 37.0 },
  { year: 2022, index: 36.0 },
  { year: 2023, index: 34.8 }
]

function App() {
  const [page, setPage] = useState('home')
  const [selectedProject, setSelectedProject] = useState(null)
  const [currentOrder, setCurrentOrder] = useState(null)
  const [orderCount, setOrderCount] = useState(0)
  const [seenOrderCount, setSeenOrderCount] = useState(0)

  const navigate = (newPage, data = null) => {
    if (newPage === 'donate' && data) setSelectedProject(data)
    if (newPage === 'status' && data) setCurrentOrder(data)
    if (newPage === 'portfolio') setSeenOrderCount(orderCount)
    setPage(newPage)
  }

  // Poll for order count updates
  useEffect(() => {
    const fetchOrderCount = () => {
      fetch(`${API}/users/1/orders`)
        .then(r => r.json())
        .then(data => setOrderCount(data.length))
        .catch(() => {})
    }
    fetchOrderCount()
    const interval = setInterval(fetchOrderCount, 5000)
    return () => clearInterval(interval)
  }, [])

  const newOrderCount = orderCount - seenOrderCount

  return (
    <>
      <Nav currentPage={page} onNavigate={navigate} newOrderCount={newOrderCount} />
      <main className="main">
        {page === 'home' && <HomePage onNavigate={navigate} />}
        {page === 'projects' && <ProjectsPage onNavigate={navigate} />}
        {page === 'portfolio' && <PortfolioPage onNavigate={navigate} />}
        {page === 'donate' && <DonatePage project={selectedProject} onNavigate={navigate} />}
        {page === 'status' && <StatusPage order={currentOrder} onUpdate={setCurrentOrder} onNavigate={navigate} />}
      </main>
    </>
  )
}

function Nav({ currentPage, onNavigate, newOrderCount }) {
  return (
    <nav className="nav">
      <div className="nav-logo" onClick={() => onNavigate('home')} style={{ cursor: 'pointer' }}>
        Charity<span>Trades</span>
      </div>
      <div className="nav-links">
        <button className={`nav-link ${currentPage === 'home' ? 'active' : ''}`} onClick={() => onNavigate('home')}>
          Dashboard
        </button>
        <button className={`nav-link ${currentPage === 'projects' ? 'active' : ''}`} onClick={() => onNavigate('projects')}>
          Orders
        </button>
        <button className={`nav-link ${currentPage === 'portfolio' ? 'active' : ''}`} onClick={() => onNavigate('portfolio')}>
          Portfolio
          {newOrderCount > 0 && <span className="nav-badge">{newOrderCount}</span>}
        </button>
      </div>
    </nav>
  )
}

function HomePage({ onNavigate }) {
  const [stats, setStats] = useState({ orders: 0, totalDonated: 0, totalImpact: 0, projects: 0 })

  useEffect(() => {
    Promise.all([
      fetch(`${API}/users/1/orders`).then(r => r.json()).catch(() => []),
      fetch(`${API}/projects`).then(r => r.json()).catch(() => [])
    ]).then(([orders, projects]) => {
      const active = orders.filter(o => ['MATCHED', 'EXECUTED', 'CLEARING', 'SETTLED'].includes(o.status))
      setStats({
        orders: active.length,
        totalDonated: active.reduce((sum, o) => sum + (o.amount || 0), 0),
        totalImpact: active.reduce((sum, o) => sum + (o.totalImpact || o.amount || 0), 0),
        projects: projects.length
      })
    })
  }, [])

  return (
    <>
      <div className="page-header">
        <h1 className="page-title">Dashboard</h1>
        <p className="page-subtitle">Donation brokerage powered by exchange mechanics</p>
      </div>

      <div className="market-stats">
        <div className="stat-card">
          <div className="stat-label">Your Orders</div>
          <div className="stat-value">{stats.orders}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Donated</div>
          <div className="stat-value">${stats.totalDonated.toFixed(2)}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Impact</div>
          <div className="stat-value green">${stats.totalImpact.toFixed(2)}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Active Projects</div>
          <div className="stat-value blue">{stats.projects}</div>
        </div>
      </div>

      <div className="lifecycle-panel">
        <div className="lifecycle-header">
          <div className="lifecycle-title">Order Lifecycle</div>
          <div className="lifecycle-hint">Hover over ⓘ to compare with traditional trading</div>
        </div>
        <div className="lifecycle-steps">
          {['NEW', 'VALIDATED', 'ROUTED', 'MATCHED', 'CLEARING', 'SETTLED'].map((step, i, arr) => (
            <span key={step} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <span className="lifecycle-step pending">
                {step}
                <InfoTooltip step={step} />
              </span>
              {i < arr.length - 1 && <span className="lifecycle-arrow">→</span>}
            </span>
          ))}
        </div>
      </div>

      <div className="two-col">
        <div className="explainer">
          <h3>How It Works</h3>
          <p>
            CharityTrades operates like a brokerage for charitable giving. When you place an order,
            it flows through the same lifecycle as a stock trade: validation, routing, matching, clearing, and settlement.
            Corporate sponsors post matching pledges (asks) on our internal order book, and your donation (bid) gets
            paired with the best available match, amplifying your impact.
          </p>
          <div style={{ marginTop: 20 }}>
            <button className="btn-primary" onClick={() => onNavigate('projects')}>
              View Market
            </button>
          </div>
        </div>

        <div className="chart-panel">
          <div className="chart-header">
            <h3>World Giving Index</h3>
            <a href="https://www.cafonline.org/about-us/publications/world-giving-index" target="_blank" rel="noopener noreferrer" className="chart-source">
              Source: CAF World Giving Index
            </a>
          </div>
          <p className="chart-explainer">Global participation rate in giving behaviors (donating money, volunteering, helping strangers)</p>
          <GivingChart data={GIVING_INDEX_DATA} />
        </div>
      </div>
    </>
  )
}

function ProjectsPage({ onNavigate }) {
  const [projects, setProjects] = useState([])
  const [matchers, setMatchers] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      fetch(`${API}/projects`).then(r => r.json()),
      fetch(`${API}/matching-orders`).then(r => r.json())
    ]).then(([p, m]) => {
      setProjects(p)
      setMatchers(m)
      setLoading(false)
    })
  }, [])

  const getMatcherInfo = (projectId) => {
    const m = matchers.filter(m => m.projectId === projectId && m.remainingAmount > 0)
    if (m.length === 0) return null
    const bestRatio = Math.max(...m.map(x => x.matchRatio))
    const totalLiquidity = m.reduce((sum, x) => sum + x.remainingAmount, 0)
    return { ratio: bestRatio, liquidity: totalLiquidity }
  }

  const generateSymbol = (name) => {
    return name.replace(/[^A-Z]/gi, '').substring(0, 4).toUpperCase() || 'PROJ'
  }

  if (loading) {
    return <div className="empty-state"><p>Loading market data...</p></div>
  }

  return (
    <>
      <div className="page-header">
        <h1 className="page-title">Market</h1>
        <p className="page-subtitle">Select a project to place an order</p>
      </div>

      <div className="ticker-grid">
        <div className="ticker-header">
          <div>Project</div>
          <div>Min Order</div>
          <div>Match Ratio</div>
          <div>Status</div>
          <div></div>
        </div>
        {projects.map(p => {
          const match = getMatcherInfo(p.id)
          const status = match ? { label: 'Active', hasMatchers: true } : { label: 'Direct', hasMatchers: false }
          return (
            <div key={p.id} className="ticker-row">
              <div>
                <div className="ticker-symbol">{generateSymbol(p.name)}</div>
                <div className="ticker-name">{p.charityName}</div>
              </div>
              <div className="ticker-value">${p.minimumAmount}</div>
              <div>
                {match ? (
                  <span className="ticker-match">{match.ratio}:1</span>
                ) : (
                  <span className="ticker-no-match">—</span>
                )}
              </div>
              <div>
                <span className={status.hasMatchers ? 'ticker-status-active' : 'ticker-status-direct'}>
                  {status.label}
                </span>
              </div>
              <div>
                <button className="btn-trade btn-sm" onClick={() => onNavigate('donate', p)}>
                  Trade
                </button>
              </div>
            </div>
          )
        })}
      </div>
    </>
  )
}

function PortfolioPage({ onNavigate }) {
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch(`${API}/users/1/orders`)
      .then(r => r.json())
      .then(data => {
        setOrders(data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)))
        setLoading(false)
      })
      .catch(() => setLoading(false))
  }, [])

  const getStatusBadge = (status) => {
    const badges = {
      NEW: 'badge-gray',
      VALIDATED: 'badge-blue',
      ROUTED: 'badge-blue',
      MATCHED: 'badge-green',
      EXECUTED: 'badge-yellow',
      CLEARING: 'badge-yellow',
      SETTLED: 'badge-green',
      REJECTED: 'badge-red'
    }
    return badges[status] || 'badge-gray'
  }

  if (loading) {
    return <div className="empty-state"><p>Loading orders...</p></div>
  }

  return (
    <>
      <div className="page-header">
        <h1 className="page-title">Orders</h1>
        <p className="page-subtitle">Your order history</p>
      </div>

      {orders.length === 0 ? (
        <div className="empty-state">
          <h3>No orders yet</h3>
          <p>Place your first order to get started</p>
          <button className="btn-primary" style={{ marginTop: 20 }} onClick={() => onNavigate('projects')}>
            View Market
          </button>
        </div>
      ) : (
        <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
          <table>
            <thead>
              <tr>
                <th>Order ID</th>
                <th>Project</th>
                <th>Amount</th>
                <th>Impact</th>
                <th>Status</th>
                <th>Route</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {orders.map(order => (
                <tr key={order.id}>
                  <td style={{ fontWeight: 600 }}>#{order.id}</td>
                  <td>{order.projectName}</td>
                  <td style={{ fontVariantNumeric: 'tabular-nums' }}>${order.amount}</td>
                  <td style={{ color: 'var(--accent-green)', fontWeight: 600, fontVariantNumeric: 'tabular-nums' }}>
                    ${order.totalImpact || order.amount}
                  </td>
                  <td><span className={`badge ${getStatusBadge(order.status)}`}>{order.status}</span></td>
                  <td style={{ color: 'var(--text-muted)' }}>{order.routeType || '—'}</td>
                  <td>
                    {['MATCHED', 'EXECUTED', 'CLEARING'].includes(order.status) && (
                      <button className="btn-secondary btn-sm" onClick={() => onNavigate('status', order)}>
                        Continue
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  )
}

function DonatePage({ project, onNavigate }) {
  const [amount, setAmount] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    try {
      const res = await fetch(`${API}/orders`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: 1, projectId: project.id, amount: parseFloat(amount) })
      })
      const data = await res.json()

      if (res.ok) {
        if (data.status === 'REJECTED') {
          setError(data.statusMessage || 'Order rejected')
        } else {
          onNavigate('status', data)
        }
      } else {
        setError(data.error || 'Failed to place order')
      }
    } catch (e) {
      setError(e.message)
    }
    setLoading(false)
  }

  if (!project) {
    onNavigate('projects')
    return null
  }

  return (
    <>
      <div className="page-header">
        <button className="btn-secondary btn-sm" onClick={() => onNavigate('projects')} style={{ marginBottom: 16 }}>
          ← Back to Market
        </button>
        <h1 className="page-title">Place Order</h1>
        <p className="page-subtitle">{project.name}</p>
      </div>

      <div className="two-col">
        <div className="card">
          <h3 style={{ marginBottom: 12, fontSize: 18 }}>Project Info</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: 14, marginBottom: 16, lineHeight: 1.7 }}>
            {project.description?.substring(0, 400)}...
          </p>
          <div style={{ fontSize: 14, color: 'var(--text-muted)' }}>
            <div style={{ marginBottom: 4 }}>Charity: <span style={{ color: 'var(--text-primary)' }}>{project.charityName}</span></div>
            <div>Minimum Order: <span style={{ color: 'var(--text-primary)' }}>${project.minimumAmount}</span></div>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="order-panel">
          <h3>Order Ticket</h3>
          <div className="form-group">
            <label className="form-label">Order Amount</label>
            <div className="amount-input-wrapper">
              <input
                type="number"
                value={amount}
                onChange={e => setAmount(e.target.value)}
                min={project.minimumAmount}
                step="0.01"
              />
            </div>
          </div>
          <button type="submit" className="btn-primary" disabled={loading} style={{ width: '100%' }}>
            {loading ? 'Submitting...' : 'Submit Order'}
          </button>
          {error && <div className="error-message">{error}</div>}
        </form>
      </div>
    </>
  )
}

function StatusPage({ order, onUpdate, onNavigate }) {
  const [loading, setLoading] = useState(false)

  const handleClearing = async () => {
    setLoading(true)
    const res = await fetch(`${API}/orders/${order.id}/clearing`, { method: 'POST' })
    const data = await res.json()
    onUpdate(data)
    setLoading(false)
    window.open(order.donationLink, '_blank')
  }

  const handleSettle = async () => {
    setLoading(true)
    const res = await fetch(`${API}/orders/${order.id}/settle`, { method: 'POST' })
    const data = await res.json()
    onUpdate(data)
    setLoading(false)
  }

  if (!order) {
    onNavigate('portfolio')
    return null
  }

  return (
    <>
      <div className="page-header">
        <button className="btn-secondary btn-sm" onClick={() => onNavigate('portfolio')} style={{ marginBottom: 16 }}>
          ← Back to Orders
        </button>
        <h1 className="page-title">Order #{order.id}</h1>
        <p className="page-subtitle">{order.projectName}</p>
      </div>

      <LifecycleVisualization status={order.status} />

      {order.statusMessage && (
        <div className="status-message">{order.statusMessage}</div>
      )}

      <div className="two-col" style={{ marginTop: 24 }}>
        <div className="order-details">
          <div className="order-row">
            <span className="order-label">Status</span>
            <span className="order-value">{order.status}</span>
          </div>
          <div className="order-row">
            <span className="order-label">Route Type</span>
            <span className="order-value">{order.routeType || 'Pending'}</span>
          </div>
          <div className="order-row">
            <span className="order-label">Order Amount</span>
            <span className="order-value">${order.amount}</span>
          </div>
          <div className="order-row">
            <span className="order-label">Matched Amount</span>
            <span className="order-value">${order.matchedAmount || 0}</span>
          </div>
          <div className="order-row">
            <span className="order-label">Total Impact</span>
            <span className="order-value large">${order.totalImpact || order.amount}</span>
          </div>
          {order.matchedWithCorporate && (
            <div className="order-row">
              <span className="order-label">Matched With</span>
              <span className="order-value">{order.matchedWithCorporate}</span>
            </div>
          )}
        </div>

        <div>
          {(order.status === 'MATCHED' || order.status === 'EXECUTED') && (
            <div className="action-panel">
              <div className="action-title">Step 1: Complete Payment</div>
              <div className="action-desc">Click below to complete your donation on GlobalGiving</div>
              <button className="btn-primary" onClick={handleClearing} disabled={loading}>
                {loading ? 'Processing...' : 'Go to GlobalGiving'}
              </button>
            </div>
          )}

          {order.status === 'CLEARING' && (
            <div className="action-panel">
              <div className="action-title">Step 2: Confirm Settlement</div>
              <div className="action-desc">After completing payment, confirm to settle the order</div>
              <button className="btn-primary" onClick={handleSettle} disabled={loading}>
                {loading ? 'Settling...' : 'Confirm Donation Complete'}
              </button>
            </div>
          )}

          {order.status === 'SETTLED' && (
            <div className="success-panel">
              <h3>Order Settled</h3>
              <p style={{ color: 'var(--text-secondary)' }}>Your donation has been confirmed</p>
            </div>
          )}
        </div>
      </div>
    </>
  )
}

function LifecycleVisualization({ status }) {
  const stages = ['NEW', 'VALIDATED', 'ROUTED', 'MATCHED', 'CLEARING', 'SETTLED']
  const statusMap = { NEW: 0, VALIDATED: 1, ROUTED: 2, MATCHED: 3, EXECUTED: 3, CLEARING: 4, SETTLED: 5, REJECTED: -1 }
  const currentIndex = statusMap[status] ?? -1

  if (status === 'REJECTED') {
    return (
      <div className="lifecycle-panel">
        <div className="lifecycle-steps">
          <span className="lifecycle-step rejected">ORDER REJECTED</span>
        </div>
      </div>
    )
  }

  return (
    <div className="lifecycle-panel">
      <div className="lifecycle-header">
        <div className="lifecycle-title">Trade Lifecycle</div>
        <div className="lifecycle-hint">Hover over ⓘ to compare with traditional trading</div>
      </div>
      <div className="lifecycle-steps">
        {stages.map((step, i) => {
          let className = 'lifecycle-step '
          if (i < currentIndex) className += 'completed'
          else if (i === currentIndex) className += 'current'
          else className += 'pending'

          const displayStep = i === 3 && status === 'EXECUTED' ? 'EXECUTED' : step

          return (
            <span key={step} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <span className={className}>
                {displayStep}
                <InfoTooltip step={displayStep} />
              </span>
              {i < stages.length - 1 && <span className="lifecycle-arrow">→</span>}
            </span>
          )
        })}
      </div>
    </div>
  )
}

function InfoTooltip({ step }) {
  const info = LIFECYCLE_INFO[step]
  if (!info) return null

  return (
    <span className="tooltip">
      <span className="info-icon">i</span>
      <span className="tooltip-text">
        <div className="tooltip-title">{info.title}</div>
        <div className="tooltip-row">
          <div className="tooltip-label">Traditional Trading</div>
          <div>{info.traditional}</div>
        </div>
        <div className="tooltip-row" style={{ marginTop: 8 }}>
          <div className="tooltip-label">CharityTrades</div>
          <div>{info.here}</div>
        </div>
      </span>
    </span>
  )
}

function GivingChart({ data }) {
  const width = 400
  const height = 200
  const padding = { top: 20, right: 20, bottom: 30, left: 45 }
  const chartWidth = width - padding.left - padding.right
  const chartHeight = height - padding.top - padding.bottom

  const minIndex = Math.min(...data.map(d => d.index)) - 2
  const maxIndex = Math.max(...data.map(d => d.index)) + 2

  const xScale = (i) => padding.left + (i / (data.length - 1)) * chartWidth
  const yScale = (val) => padding.top + chartHeight - ((val - minIndex) / (maxIndex - minIndex)) * chartHeight

  // Create path for line
  const linePath = data.map((d, i) => `${i === 0 ? 'M' : 'L'} ${xScale(i)} ${yScale(d.index)}`).join(' ')

  // Create area path
  const areaPath = `${linePath} L ${xScale(data.length - 1)} ${height - padding.bottom} L ${padding.left} ${height - padding.bottom} Z`

  return (
    <svg viewBox={`0 0 ${width} ${height}`} className="giving-chart">
      {/* Grid lines */}
      {[30, 33, 36, 39].map(val => (
        <g key={val}>
          <line
            x1={padding.left}
            y1={yScale(val)}
            x2={width - padding.right}
            y2={yScale(val)}
            stroke="var(--border-color)"
            strokeDasharray="3,3"
          />
          <text x={padding.left - 8} y={yScale(val) + 4} className="chart-label" textAnchor="end">
            {val}%
          </text>
        </g>
      ))}

      {/* Area fill */}
      <path d={areaPath} fill="url(#chartGradient)" />

      {/* Line */}
      <path d={linePath} fill="none" stroke="var(--accent-green)" strokeWidth="2" />

      {/* Data points */}
      {data.map((d, i) => (
        <circle
          key={d.year}
          cx={xScale(i)}
          cy={yScale(d.index)}
          r="4"
          fill="var(--bg-primary)"
          stroke="var(--accent-green)"
          strokeWidth="2"
        />
      ))}

      {/* X-axis labels: 2010, 2013, 2016, 2019, Present */}
      {[0, 3, 6, 9].map(i => (
        <text key={data[i].year} x={xScale(i)} y={height - 8} className="chart-label" textAnchor="middle">
          {data[i].year}
        </text>
      ))}
      <text x={xScale(data.length - 1)} y={height - 8} className="chart-label" textAnchor="middle">
        Present
      </text>

      {/* Gradient definition */}
      <defs>
        <linearGradient id="chartGradient" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="var(--accent-green)" stopOpacity="0.3" />
          <stop offset="100%" stopColor="var(--accent-green)" stopOpacity="0" />
        </linearGradient>
      </defs>
    </svg>
  )
}

export default App
